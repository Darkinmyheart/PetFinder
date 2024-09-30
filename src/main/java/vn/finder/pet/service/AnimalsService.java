package vn.finder.pet.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.finder.pet.common.ImageCommon;
import vn.finder.pet.dao.AnimalsDAO;
import vn.finder.pet.dto.DtoPetShelters;
import vn.finder.pet.entity.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class AnimalsService {
    private AnimalsDAO animalsDAO;
    private UsersService usersService;
    private BreedService breedService;

    @Autowired
    public AnimalsService(AnimalsDAO animalsDAO, UsersService usersService, BreedService breedService) {
        this.animalsDAO = animalsDAO;
        this.usersService = usersService;
        this.breedService = breedService;
    }

    public Animals findById(Long id){
        return this.animalsDAO.findById(id).get();
    }

    public Page<Animals> searchAnimals(List<String> breedType, String breedName, String shelterAddress, List<String> ages, List<Boolean> genders, String size, String name, List<Long> adopt, int page, int sizePage) {
        Pageable pageable = PageRequest.of(page, sizePage);
        return animalsDAO.findByBreedNameContains(breedType, breedName, shelterAddress, ages, genders, size, name, adopt, pageable);
    }

    public Page<Animals> findAll(int page, int sizePage) {
        Pageable pageable = PageRequest.of(page, sizePage);
        return animalsDAO.findAll(pageable);
    }

    public Page<Animals> filterName(int page, int sizePage) {
        Pageable pageable = PageRequest.of(page, sizePage);
        return animalsDAO.filterName(pageable);
    }

    public Page<Animals> filterDate(int page, int sizePage) {
        Pageable pageable = PageRequest.of(page, sizePage);
        return animalsDAO.filterDate(pageable);
    }

    public Page<Animals> filterFavorite(int page, int sizePage) {
        Pageable pageable = PageRequest.of(page, sizePage);
        return animalsDAO.filterFavorite(pageable);
    }

    public Integer findCountAnimalsAvailable(String email){
        Users users = this.usersService.findById(email).get();
        return this.animalsDAO.findCountAnimalsAvailable(users.getShelters().getId());
    }

    public Integer findCountAnimalsInShelter(String email){
        Users users = this.usersService.findById(email).get();
        return this.animalsDAO.findCountAnimalsInShelter(users.getShelters().getId());
    }

    public Integer findCountAnimalsAdopted(String email){
        Users users = this.usersService.findById(email).get();
        return this.animalsDAO.findCountAnimalsAdopted(users.getShelters().getId());
    }

    public Integer findCountAwaitingAnimals(String email){
        Users users = this.usersService.findById(email).get();
        return this.animalsDAO.findCountAwaitingAnimals(users.getShelters().getId());
    }

    public Integer findCountAdopt(String email){
        Users users = this.usersService.findById(email).get();
        return this.animalsDAO.findCountAdopt(users.getShelters().getId());
    }

    public Page<Adopt> findByStatus(String email, String status, int page, int sizePage){
        Pageable pageable = PageRequest.of(page, sizePage);
        Users users = this.usersService.findById(email).get();
        return this.animalsDAO.findByStatus(status, pageable, users.getShelters().getId());
    }

    public Integer findCountFavorite(String email){
        Users users = this.usersService.findById(email).get();
        return this.animalsDAO.findCountFavorite(users.getShelters().getId());
    }

    public Page<Animals> findAllPet(String email, int page, int sizePage){
        Pageable pageable = PageRequest.of(page, sizePage);
        Users users = this.usersService.findById(email).get();
        return this.animalsDAO.findAllPet(pageable, users.getShelters().getId());
    }

    public Page<Animals> findRandom(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return this.animalsDAO.findRandom(pageable);
    }

    public Page<Animals> findByShelterAddressOrderByCustom(int page, int size, String shelterAddress){
        Pageable pageable = PageRequest.of(page, size);
        return this.animalsDAO.findByShelterAddressOrderByCustom(shelterAddress, pageable);
    }

    public Page<Animals> findByBreedOrderByCustom(String breedName, String breedType, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return animalsDAO.findByBreedOrderByCustom(breedName, breedType, pageable);
    }

    public Animals checkAnimal(String breedName, String breedType,String animalName,boolean animalGender,String animalSize,String animalAge){
        Animals animals = animalsDAO.findAnimalsByCriteria(breedName,breedType,animalName,animalGender,animalSize,animalAge);
        return animals;
    }

    @Transactional
    public Animals save(Animals animal,List<MultipartFile> file){
        try {
            Animals animalF = animalsDAO.save(animal);
            try {
                List<String> listUrl=ImageCommon.saveImage(file,"./imgAnimal");
                animalF.setAnimalAvatar(String.join(",", listUrl));
                animalsDAO.save(animalF);
            }catch (IOException e){
                System.out.println(e);
            }
        } catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    @Transactional
    public Boolean updateAnimal(DtoPetShelters dps, Long id){
        Optional<Animals> animals = this.animalsDAO.findById(id);
        if(!animals.isEmpty()){
            if(this.breedService.findByBreedTypeAndBreedName(dps.getBreed_type(), dps.getBreed_name()) == null){
                Breed db = new Breed();
                db.setBreed_type(dps.getBreed_type());
                db.setBreed_name(dps.getBreed_name());
                this.breedService.save(db);
                animals.get().setBreed(db);
            } else {
                animals.get().setBreed(this.breedService.findByBreedTypeAndBreedName(dps.getBreed_type(), dps.getBreed_name()));
            }
            animals.get().setAnimalName(dps.getAnimalName());
            animals.get().setAnimalSize(dps.getAnimalSize());
            animals.get().setAnimalAge(dps.getAnimalAge());
            animals.get().setAnimalGender(dps.isAnimalGender());
//            animals.get().setAnimalAvatar(dps.getAnimalAvatar());
            animals.get().getAnimalInfo().setAnimalInfoColor(dps.getAnimal_info_color());
            animals.get().getAnimalInfo().setAnimalInfoLeg(dps.getAnimal_info_leg());
            animals.get().getAnimalInfo().setAnimalInfoHarmony(dps.getAnimal_info_harmony());
            animals.get().getAnimalInfo().setAnimalInfoHealth(dps.getAnimal_info_health());
            animals.get().getAnimalInfo().setAnimalInfoCharacteristics(dps.getAnimal_info_characteristics());
            animals.get().getAnimalInfo().setAnimalInfoDescription(dps.getAnimal_info_description());

//            List<Avatar> av= new ArrayList<>();
//            dps.getListAvatar().forEach(e ->{
//                Avatar a = new Avatar();
//                a.setAvatar_name(e);
//                a.setAnimals(animals.get());
//                av.add(a);
//            } );
//            animals.get().setListAvatar(av);

//            this.animalsDAO.save(animals.get());
        }
        return false;
    }

    @Transactional
    public Boolean deleteAnimal(Long id){
        Optional<Animals> animals = this.animalsDAO.findById(id);
        if(!animals.isEmpty()){
            this.animalsDAO.delete(animals.get());
            return true;
        }
        return false;
    }

    public String petGender(boolean gender){
        String gd = "";
        if(gender == true){
            gd = "Giống Đực";
        }else {
            gd = "Giống Cái";
        }
        return gd;
    }

    public String transGenderToVN(Boolean gender){
        return gender ? "Cái" : "Đực";
    }

    public String transAgeToVN(String age){
        switch (age) {
            case "Newborn":
                return "Sơ sinh";
            case "Young":
                return "Trẻ";
            case "Adult":
                return "Trưởng thành";
            case "Senior":
                return "Già";
            default:
                return age;
        }
    }

    public String transSizeToVN(String size){
        switch (size) {
            case "Small":
                return "Nhỏ";
            case "Medium":
                return "Trung bình";
            case "Large":
                return "Lớn";
            case "Extra Large":
                return "Rất lớn";
            default:
                return size;
        }
    }


    public String transHeathToVN(String heath){
        switch (heath) {
            case "Vaccinated":
                return "Đã tiêm vaccin";
            case "Dewormed":
                return "Đã sổ giun";
            case "Spayed":
                return "Đã thiến";
            default:
                return heath;
        }
    }

    public String transHarmonyToVN(String heath){
        switch (heath) {
            case "Dog":
                return "Chó";
            case "Cat":
                return "Mèo";
            case "Children":
                return "Trẻ con";
            default:
                return heath;
        }
    }
}
