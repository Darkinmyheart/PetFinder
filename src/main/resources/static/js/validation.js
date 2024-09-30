// VALIDATION
function Validator(job) {
	var workplace = document.querySelector(job.workplace);
	var listRule = {};
	
	workplace.onsubmit = function(e) {
		e.preventDefault();
		
		var error = [];
		var validPassword = false;

		job.rules.forEach(function(rule) {
			var workplaceElement = workplace.querySelector(rule.select);
			
			var isValid = validate(workplaceElement, rule);
			if(isValid !== undefined){
				error.push(true);
			} else {
				error.push(false);
			}
		});

        workplace.querySelectorAll(".invalid-feedback").forEach(function(element){
            element.classList.add("d-block")
        })

		try {
			try {
				var pass = workplace.querySelector("input[type='password'].password").value;
			} catch (error) {
				var pass = workplace.querySelector("input[type='text'].password").value;
			}

			if(getParentElement(document.querySelector(".password"), ".form-group").querySelector(".invalid-feedback").innerText === ""){
				getParentElement(password, ".form-group").querySelector(".invalid-feedback").innerText = pass.trim() === "" ? "This value should not be empty." : ``
			}

			if(getParentElement(document.querySelector(".password"), ".form-group").querySelector(".invalid-feedback").innerText === ""){
				getParentElement(document.querySelector(".password"), ".form-group").querySelector(".invalid-feedback").innerText = (pass.trim().length < 21 && pass.trim().length > 7) ? "" : `Mật khẩu phải từ 8 đến 20 ký tự.`
			}

			if(getParentElement(document.querySelector(".password"), ".form-group").querySelector(".invalid-feedback").innerText === ""){
				getParentElement(document.querySelector(".password"), ".form-group").querySelector(".invalid-feedback").innerText = checkPasswordRequirements(pass) ? "" : `Mật khảu phải chứa ít nhất 3 trong 4 điều sau: \n1. Ký tự viết thường\n2. Ký tự viết hoa\n3. Ký tự đặc biệt\n4. Ký tự số`
			}
			if(getParentElement(document.querySelector(".password"), ".form-group").querySelector(".invalid-feedback").innerText !== ""){
				validPassword = true;
			}
		} catch (error) {

		}

		var i = 0;
		error.forEach(e => {
			if(e){
				i++;
			}
		});

		if(i === 0){
			if(!validPassword){
				workplace.submit();
			}
		}
	}

	function getParent(workplaceElement, parent) {
		while(workplaceElement.parentElement){
			if(workplaceElement.parentElement.matches(parent)) {
				return workplaceElement.parentElement;
			}
			workplaceElement = workplaceElement.parentElement;
		}
	}

	function validate(workplaceElement, rule) {
		var errorMessage = "";
		for(var i = 0; i < listRule[rule.select].length; i++){
			switch(workplaceElement.type){
					case "radio":
					case "checkbox":
						errorMessage = listRule[rule.select][i](workplace.querySelector(rule.select + ":checked") ? workplace.querySelector(rule.select + ":checked").value : undefined)
						break;
					default:
						errorMessage = listRule[rule.select][i](workplaceElement.value)
						break;

			};
			if(errorMessage){
				break;
			}
		}

		if(errorMessage){
			getParent(workplaceElement, job.formGroupSelector).querySelector(".invalid-feedback").innerHTML = errorMessage;
			getParent(workplaceElement, job.formGroupSelector).querySelector(".invalid-feedback").style.color = "red";
		} else {
			getParent(workplaceElement, job.formGroupSelector).querySelector(".invalid-feedback").innerHTML = "";
		}

		return errorMessage;
	}

	job.rules.forEach(function(rule) {
		var workplaceElements = workplace.querySelectorAll(rule.select);

		if(Array.isArray(listRule[rule.select])){
			listRule[rule.select].push(rule.test)
		} else {
			listRule[rule.select] = [rule.test];
		}

		Array.from(workplaceElements).forEach(function(workplaceElement){
			workplaceElement.onblur = function() {
				validate(workplaceElement, rule);

                workplace.querySelectorAll(".invalid-feedback").forEach(function(element){
                    element.classList.add("d-block")
                })
			};

            workplaceElement.oninput = function () {
                var errorElement = getParent(workplaceElement, job.formGroupSelector).querySelector(".invalid-feedback");
                errorElement.innerText = "";
            }
		})
	})
}

// RULE
Validator.isRequired = function(select, message) {
	return {
		select: select,
		test: function(value) {
			return value ? undefined : message || "Vui lòng nhập trường này";
		}
	}
}

Validator.isEmail = function(select, message) {
	return {
		select: select,
		test: function(value) {
			var regex = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
			return regex.test(value) ? undefined :  message || 'Trường này phải là email';
		}
	}
}


Validator.isLengthMin = function(select, minValue, message) {
	return {
		select: select,
		test: function(value) {
			var min = minValue ? minValue : 10
			return value.length == min ? undefined :  message || `Trường này phải chứa ít nhất ${min} ký tự`;
		}
	}
}

Validator.isConfirm = function(select, confirm, message) {
	return {
		select: select,
		test: function(value) {
			return value == confirm() ? undefined :  message || `Trường này nhập không đúng`;
		}
	}
}

Validator.isPhoneNumber = function(select, message){
	return {
		select: select,
		test: function(value) {
			var regex =  /^(0|\+84)[1-9][0-9]{8}$/;
			return regex.test(value) ? undefined :  message || 'Số điện thoại sai định dạng';
		}
	}
}


/*

*/
// Validation password
function checkPasswordRequirements(password) {
	const hasLowercase = /[a-z]/.test(password);
	const hasUppercase = /[A-Z]/.test(password);
	const hasNumber = /\d/.test(password);
	const hasSpecial = /[!@#$%^&*(),.?":{}|<>]/.test(password);

	// Count the number of requirements met
	const requirementsMet = [hasLowercase, hasUppercase, hasNumber, hasSpecial].filter(Boolean).length;

	return requirementsMet >= 3;
}

function getParentElement(workplaceElement, parent) {
	while(workplaceElement.parentElement){
		if(workplaceElement.parentElement.matches(parent)) {
			return workplaceElement.parentElement;
		}
		workplaceElement = workplaceElement.parentElement;
	}
}

var password = document.querySelector('.password');
if(password != null){

	console.log()

	document.querySelector('.password').onfocus = function (params) {
		document.querySelector(".valid-password").classList.remove("d-none");
	}

	document.querySelector('.password').onblur = function (params) {
		document.querySelector(".valid-password").classList.add("d-none");

		var errorMessage = false;
		try {
			var pass = document.querySelector("input[type='password'].password").value;
		} catch (error) {
			var pass = document.querySelector("input[type='text'].password").value;
		}

		if(pass.trim() === ""){
			getParentElement(password, ".form-group").querySelector(".invalid-feedback").innerText = "This value should not be empty.";
			errorMessage = true;
		} else {
			getParentElement(password, ".form-group").querySelector(".invalid-feedback").innerText = ``;
			errorMessage = false;
		}
		getParentElement(password, ".form-group").querySelector(".invalid-feedback").classList.add('d-block');


		if(!errorMessage){
			if(pass.trim().length > 7 && pass.trim().length < 21){
				getParentElement(password, ".form-group").querySelector(".invalid-feedback").innerText = ``;
				errorMessage = false;
			} else {
				getParentElement(password, ".form-group").querySelector(".invalid-feedback").innerText = `Mật khẩu phải từ 8 đến 20 ký tự.`;
				errorMessage = true;
			}
		}

		if(!errorMessage){
			if(checkPasswordRequirements(pass)){
				getParentElement(password, ".form-group").querySelector(".invalid-feedback").innerText = ``;
				errorMessage = false;
			} else {
				getParentElement(password, ".form-group").querySelector(".invalid-feedback").innerText = `Mật khảu phải chứa ít nhất 3 trong 4 điều sau: \n1. Ký tự viết thường\n2. Ký tự viết hoa\n3. Ký tự đặc biệt\n4. Ký tự số`;
				errorMessage = true;
			}
		}
	}
	
	var validPassword = {
		charLength: document.querySelector('.valid-password .length'),
		lowercase: document.querySelector('.valid-password .lowercase'),
		uppercase: document.querySelector('.valid-password .uppercase'),
		number: document.querySelector('.valid-password .number'),
		special: document.querySelector('.valid-password .special')
	};
	
	var pattern = {
	
		charLength: function () {
			if (password.value.length >= 8 && password.value.length <= 20) {
				return true;
			}
		},
	
		lowercase: function () {
			var regex = /^(?=.*[a-z]).+$/; // Lowercase character pattern
			if (regex.test(password.value)) {
				return true;
			}
		},
	
		uppercase: function () {
			var regex = /^(?=.*[A-Z]).+$/; // Uppercase character pattern
			if (regex.test(password.value)) {
				return true;
			}
		},
	
		number: function () {
			var regex = /^(?=.*[0-9]).+$/; // Number check
			if (regex.test(password.value)) {
				return true;
			}
		},
	
		special: function () {
			var regex = /^(?=.*[_\W]).+$/; // Special character 
			if (regex.test(password.value)) {
				return true;
			}
		}
	};
	
	// Listen for keyup action on password field
	password.addEventListener('keyup', function () {
		patternTest(pattern.charLength(), validPassword.charLength);
		patternTest(pattern.lowercase(), validPassword.lowercase);
		patternTest(pattern.uppercase(), validPassword.uppercase);
		patternTest(pattern.number(), validPassword.number);
		patternTest(pattern.special(), validPassword.special);
	
		// Check that all requirements are fulfilled
		if (hasClass(validPassword.charLength, 'valid')) {
			var i = 0;
			if (document.querySelectorAll(".true3in4 li").forEach(e => {
				if (e.classList[1] === "valid") {
					i++;
				}
			}))
	
			console.log(i >= 3)
			if (i >= 3) {
				addClass(password.parentElement, 'valid');
				document.querySelector(".batbuoc").classList.add("d-none");
				document.querySelector(".true3in4").classList.add("d-none");
	
				document.querySelector(".trueValid").classList.remove("d-none");
			} else {
				removeClass(password.parentElement, 'valid');
				document.querySelector(".batbuoc").classList.remove("d-none");
				document.querySelector(".true3in4").classList.remove("d-none");
	
				document.querySelector(".trueValid").classList.add("d-none");
			}
		}
		else {
			removeClass(password.parentElement, 'valid');
			document.querySelector(".batbuoc").classList.remove("d-none");
			document.querySelector(".true3in4").classList.remove("d-none");
	
			document.querySelector(".trueValid").classList.add("d-none");
		}
	});
	
	
	// Pattern Test function
	function patternTest(pattern, response) {
		if (pattern) {
			addClass(response, 'valid');
		}
		else {
			removeClass(response, 'valid');
		}
	}
	
	// Has Class Function 
	function hasClass(el, className) {
		if (el.classList) {
			return el.classList.contains(className);
		}
		else {
			new RegExp('(^| )' + className + '( |$)', 'gi').test(el.className);
		}
	}
	
	// Add Class Function
	function addClass(el, className) {
		if (el.classList) {
			el.classList.add(className);
		}
		else {
			el.className += ' ' + className;
		}
	}
	
	// Remove Class Function
	function removeClass(el, className) {
		if (el.classList)
			el.classList.remove(className);
		else
			el.className = el.className.replace(new RegExp('(^|\\b)' + className.split(' ').join('|') + '(\\b|$)', 'gi'), ' ');
	}

}