package vn.finder.pet.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.finder.pet.common.ImageCommon;
import vn.finder.pet.dao.ShelterDAO;
import vn.finder.pet.dto.DtoAddShelters;
import vn.finder.pet.dto.DtoPetShelters;
import vn.finder.pet.dto.ShelterDTO;
import vn.finder.pet.entity.*;

import java.awt.*;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class SheltersService {
    private ShelterDAO shelterDAO;
    private BreedService breedService;
    private AnimalsService animalsService;
    private AnimalInfoService animalInfoService;
    private UsersService usersService;

    @Autowired
    public SheltersService(ShelterDAO shelterDAO, BreedService breedService, AnimalsService animalsService, AnimalInfoService animalInfoService, UsersService usersService) {
        this.shelterDAO = shelterDAO;
        this.breedService = breedService;
        this.animalsService = animalsService;
        this.animalInfoService = animalInfoService;
        this.usersService = usersService;
    }

    public List<Shelters> findByShelterStatus(String awaiting) {
        return this.shelterDAO.findByShelterStatus(awaiting);
    }

    public Page<Shelters> findAll(int page, int sizePage) {
        Pageable pageable = PageRequest.of(page, sizePage);
        return this.shelterDAO.findSheltersByStatusNotContaining(pageable, "Awaiting");
    }

    public Page<Shelters> findSheltersByStatusContaining(String status, int page, int sizePage) {
        Pageable pageable = PageRequest.of(page, sizePage);
        return this.shelterDAO.findSheltersByStatusContaining(pageable, status);
    }

    public Page<Shelters> findByShelterNameAndShelterAddress(String status, String shelterName, String shelterAddress, int page, int sizePage) {
        Pageable pageable = PageRequest.of(page, sizePage);
        return this.shelterDAO.findByShelterNameAndShelterAddress(pageable, shelterName, shelterAddress, status);
    }

    public Shelters findById(Long id) {
        return this.shelterDAO.findById(id).get();
    }

    @Transactional
    public void createShelter(DtoPetShelters dto, String email, List<MultipartFile> filesAvatar) {



        Users users = this.usersService.findById(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Animals animals = new Animals();
        animals.setShelters(users.getShelters());
        animals.setAnimalAge(dto.getAnimalAge());
        animals.setAnimalAvatar(dto.getAnimalAvatar());
        animals.setAnimalDate(Date.valueOf(LocalDate.now()));
        animals.setAnimalGender(dto.isAnimalGender());
        animals.setAnimalName(dto.getAnimalName());
        animals.setAnimalSize(dto.getAnimalSize());
        List<Avatar> av = new ArrayList<>();
        dto.getListAvatar().forEach(e -> {
            Avatar a = new Avatar();
            a.setAvatar_name(e);
            a.setAnimals(animals);
            av.add(a);
        });
        animals.setListAvatar(av);
        //set breed
        Breed db = breedService.findByBreedTypeAndBreedName(dto.getBreed_type(),dto.getBreed_name());
        if(db!=null){
            animals.setBreed(db);
        }else{
            db.setBreed_name(dto.getBreed_name());
            db.setBreed_type(dto.getBreed_type());
            breedService.save(db);
            animals.setBreed(db);
        }
        this.animalsService.save(animals, filesAvatar);
        AnimalInfo info = new AnimalInfo();
        info.setAnimalInfoColor(dto.getAnimal_info_color());
        info.setAnimalInfoHarmony(dto.getAnimal_info_harmony());
        info.setAnimalInfoHealth(dto.getAnimal_info_health());
        info.setAnimalInfoLeg(dto.getAnimal_info_leg());
        info.setAnimalInfoCharacteristics(dto.getAnimal_info_characteristics());
        info.setAnimalInfoDescription(dto.getAnimal_info_description());
        info.setAnimals(animals);
        this.animalInfoService.save(info);
    }

    public Boolean updateShelter(Long id, String status) {
        Optional<Shelters> shelters = this.shelterDAO.findById(id);
        if (!shelters.isEmpty()) {
            shelters.get().setShelterStatus(status);
            this.shelterDAO.save(shelters.get());
            return true;
        }
        return false;
    }

    @Transactional
    public void addNewShelter(DtoAddShelters shelters, String email, MultipartFile imageShelter) {
        Shelters sh = new Shelters();
        Users users = this.usersService.findById(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        sh.setUsers(users);
        sh.setShelterAvatar(shelters.getImg());
        sh.setShelterAddress(shelters.getLocation());
        sh.setShelterEmail(shelters.getAddressemail());
        sh.setShelterDate(Date.valueOf(LocalDate.now()));
        sh.setShelterInfoFacebook(shelters.getLinkfacebook());
        sh.setShelterInfoInstagram(shelters.getLinkintagram());
        sh.setShelterPhone(shelters.getphonenumber());
        sh.setShelterName(shelters.getNamne());
        sh.setShelterInfoOperatingTime(shelters.getOpentime());
        sh.setShelterDescription(shelters.getShortdescription());
        sh.setShelterLatitude(shelters.getLatitude());
        sh.setShelterLongitude(shelters.getLongitude());
        sh.setShelterStatus("Awaiting");
        sh.setShelterInfoPolicy(shelters.getListpolicy());
        sh.setShelterInfoMission(shelters.getListmission());
        List<MultipartFile> listImg = new ArrayList<>();
        listImg.add(imageShelter);
        List<String> listNameImg = new ArrayList<>();
        try {
            listNameImg = ImageCommon.saveImage(listImg, "./imgShelter");
            sh.setShelterAvatar(listNameImg.get(0));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.shelterDAO.save(sh);
    }

    public Page<Animals> findAllHistoryActiveOfShelter(int page, int size, String userName) {
        Pageable pageable = PageRequest.of(page, size);
        return this.shelterDAO.findAllHistoryActiveOfShelter(pageable, userName);
    }

    public String extractCity(String address) {
        String[] cities = {
                "An Giang", "Bà Rịa - Vũng Tàu", "Bạc Liêu", "Bắc Kạn", "Bắc Giang", "Bắc Ninh",
                "Bến Tre", "Bình Dương", "Bình Định", "Bình Phước", "Bình Thuận", "Cà Mau",
                "Cao Bằng", "Cần Thơ", "Đà Nẵng", "Đắk Lắk", "Đắk Nông", "Điện Biên", "Đồng Nai",
                "Đồng Tháp", "Gia Lai", "Hà Giang", "Hà Nam", "Hà Nội", "Hà Tĩnh", "Hải Dương",
                "Hải Phòng", "Hậu Giang", "Hòa Bình", "Hưng Yên", "Khánh Hòa", "Kiên Giang",
                "Kon Tum", "Lai Châu", "Lâm Đồng", "Lạng Sơn", "Lào Cai", "Long An", "Nam Định",
                "Nghệ An", "Ninh Bình", "Ninh Thuận", "Phú Yên", "Quảng Bình",
                "Quảng Nam", "Quảng Ngãi", "Quảng Ninh", "Quảng Trị", "Sóc Trăng", "Sơn La",
                "Tây Ninh", "Thái Bình", "Thái Nguyên", "Thanh Hóa", "Thừa Thiên Huế", "Tiền Giang",
                "Hồ Chí Minh", "TP. Hồ Chí Minh", "Trà Vinh", "Tuyên Quang", "Vĩnh Long", "Vĩnh Phúc", "Yên Bái"
        };

        String cityPattern = String.join("|", cities);

        Pattern pattern = Pattern.compile(cityPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(address);

        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public String transStatusToVN(String status) {
        switch (status) {
            case "Awaiting":
                return "Đang chờ";
            case "Opening":
                return "Đã đồng ý";
            case "Canceled":
                return "Đã từ chối";
            default:
                return status;
        }
    }

    public List<ShelterDTO> listShelterRegist(String sortField, String status, String ascOrDesc) {
        List<Shelters> Shelters;

        switch (sortField) {
            case "name":
                Shelters = ascOrDesc.equals("asc") ? shelterDAO.findByShelterStatusOrderByShelterNameAsc(status)
                        : shelterDAO.findByShelterStatusOrderByShelterNameDesc(status);
                break;
            case "date":
                Shelters = ascOrDesc.equals("asc") ? shelterDAO.findByShelterStatusOrderByShelterDateAsc(status)
                        : shelterDAO.findByShelterStatusOrderByShelterDateDesc(status);
                break;
            default:
                throw new IllegalArgumentException("Invalid sort field: " + sortField);
        }
        System.out.println("size  " + Shelters.size());
        return Shelters.stream()
                .map(shelter -> new ShelterDTO(
                        shelter.getId(), // Assuming the `Shelters` entity has a method `getId()`
                        shelter.getShelterAvatar(), // Map to DTO field `shelterAvatar`
                        shelter.getShelterName(), // Map to DTO field `shelterName`
                        shelter.getShelterEmail(), // Map to DTO field `shelterEmail`
                        shelter.getShelterAddress(), // Map to DTO field `shelterAddress`
                        shelter.getShelterPhone(), // Map to DTO field `shelterPhone`
                        String.valueOf(shelter.getShelterDate()), // Convert to String if needed
                        shelter.getShelterStatus(), // Map to DTO field `shelterStatus`
                        shelter.getShelterInfoMission(), // Map to DTO field `shelterInfoMission`
                        shelter.getShelterInfoPolicy(), // Map to DTO field `shelterInfoPolicy`
                        shelter.getShelterInfoOperatingTime(), // Map to DTO field `shelterInfoOperatingTime`
                        shelter.getShelterInfoFacebook(), // Map to DTO field `shelterInfoFacebook`
                        shelter.getShelterInfoInstagram(), // Map to DTO field `shelterInfoInstagram`
                        shelter.getShelterDescription(), // Map to DTO field `shelterDescription`
                        shelter.getShelterLatitude(), // Map to DTO field `shelterLatitude`
                        shelter.getShelterLongitude(), // Map to DTO field `shelterLongitude`
                        shelter.getUsers().getUserName() // Map to DTO field `userName`
                ))
                .collect(Collectors.toList());
    }
}
