function signIn() {
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;

    var signInRequest = {
        "username": username,
        "password": password
    };

    fetch("/auth/signIn", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(signInRequest),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json(); // Обрабатываем ответ в формате JSON
        })
        .then(data => {
            console.log('Успешный вход:', data);
            window.location.href = "http://localhost:8080/index";
        })
        .catch(error => {
            console.error('Ошибка входа:', error);
        });
}

function signUp() {
    var username = document.getElementById("username_register").value;
    var password = document.getElementById("password_register").value;
    var confirmPassword = document.getElementById("confirmPassword").value;
    var email = document.getElementById("email_register").value;

    var signUpRequest = {
        "username": username,
        "password": password,
        "confirmPassword": confirmPassword,
        "email": email
    };

    fetch("/auth/signUp", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(signUpRequest)
    })
        .then(response => response.json())
        .then(data => {
            console.log(data);
            window.location.href = "http://localhost:8080/auth/signIn";
        })
}