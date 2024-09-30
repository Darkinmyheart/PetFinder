const API_KEY = "AIzaSyC26q4osHMCUH-bfIbjppqJ_RST-rf5ppg";
const API_URL = `https://generativelanguage.googleapis.com/v1/models/gemini-pro:generateContent?key=${API_KEY}`;
const submitButton = document.querySelector('#btn-gpt')
const loadWrapp = document.querySelector(".load-wrapp")

async function getMessage() {
    console.log("clicked")

    const animalType = document.querySelector(".animal-type").value
    const animalBreed = document.querySelector(".animal-breed").value
    const animalName = document.querySelector(".animal-Name").value
    var harmony = ""
    document.querySelectorAll(".animalHarmony").forEach(e => {
        if(e.checked){
            harmony += e.value;
            harmony += ", ";
        }
    })
    const characteristics = document.querySelector("#petCharacteristics").value

    var text = "Hãy tưởng tượng và mô tả giúp tôi về hoàn cảnh đáng thương vì 1 lý do nào đó phải sống trong trung tâm cứu trợ động vật của động vật 'Tên: " + animalName + ", Giống: " + animalType + " " + animalBreed + ", Tính cách: " + characteristics + ", Hòa hợp với: " + harmony + "' và giúp nó tìm ngôi nhà mới.    NO YAPPING"

    try {
        const response = await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                contents: [{
                    role: "user",
                    parts: [{ text: text }]
                }]
            })
        });

        const data = await response.json();

        console.log(data?.candidates[0].content.parts[0].text);
        loadWrapp.classList.add("d-none")
        loadWrapp.classList.remove("d-flex")
        document.querySelector(".descriptions").innerHTML = `<div class="ql-editor" contenteditable="true"><p class="">${data?.candidates[0].content.parts[0].text}</p></div>`
    } catch (error) {
        console.log(error);
    }
}


submitButton.addEventListener('click', () => {
    loadWrapp.classList.remove("d-none")
    loadWrapp.classList.add("d-flex")
    document.querySelector(".ql-editor").classList.add("d-flex")
    document.querySelector(".ql-editor").classList.add("justify-content-center") 
    document.querySelector(".ql-editor").classList.add("fs-5")
    document.querySelector(".ql-editor").classList.add("letter-holder")
    
    var i = 0;
    document.querySelectorAll(".ql-editor.d-flex p").forEach(e => {
        i++;
        var s = "l-"+ i
        e.classList.add(s)
        e.classList.add("letter")
    }) 

    setTimeout(() => {
        getMessage(); 
    }, 2000)
});
