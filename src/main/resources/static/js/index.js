//Реализация диаграммы
document.addEventListener("DOMContentLoaded", function() {
    const data = {
        labels: ['Доходы', 'Расходы'],
        datasets: [{
            label: 'Доходы и расходы',
            data: [3000, 1500],
            backgroundColor: [
                'rgba(75, 192, 192, 0.2)',
                'rgba(255, 99, 132, 0.2)'
            ],
            borderColor: [
                'rgba(75, 192, 192, 1)',
                'rgba(255, 99, 132, 1)'
            ],
            borderWidth: 1
        }]
    };

    const options = {
        responsive: false,
        maintainAspectRatio: false,
        scales: {
            y: {
                beginAtZero: true
            }
        }
    };

    const ctx = document.getElementById('chart').getContext('2d');

    const myChart = new Chart(ctx, {
        type: 'bar',
        data: data,
        options: options
    });
});

//Реализация открытия и закрытия окна с добавлением нового счета
document.addEventListener('DOMContentLoaded', function() {
    const createAccountModal = document.getElementById('create-account-modal');
    const addAccountButton = document.getElementById('add-account-button');
    const closeButton = createAccountModal.querySelector('.close');

    addAccountButton.addEventListener('click', function() {
        createAccountModal.style.display = 'block';
    });

    closeButton.addEventListener('click', function() {
        createAccountModal.style.display = 'none';
    });
});


//Реализация появления кнопки удаления
document.addEventListener('DOMContentLoaded', function() {
    const accountInfos = document.querySelectorAll('.account-info');
    const operationInfos = document.querySelectorAll('.transaction');

    accountInfos.forEach(function(accountInfo) {
        const deleteButton = accountInfo.querySelector('.delete-account-button');

        accountInfo.addEventListener('mouseenter', function() {
            deleteButton.style.display = 'block';
        });

        accountInfo.addEventListener('mouseleave', function() {
            deleteButton.style.display = 'none';
        });
    });

    operationInfos.forEach(function(operationInfo) {
        const deleteButton = operationInfo.querySelector('.delete-operation-button');

        operationInfo.addEventListener('mouseenter', function() {
            deleteButton.style.visibility = 'visible';
        });

        operationInfo.addEventListener('mouseleave', function() {
            deleteButton.style.visibility = 'hidden';
        });
    });
});

//Добавления окна с подтверждением удаления счета
document.addEventListener('DOMContentLoaded', function() {
    const deleteAccountModal = document.getElementById('delete-account-modal');
    const confirmDeleteButton = document.getElementById('confirm-delete-account');
    const closeButton = deleteAccountModal.querySelector('.close');
    const closeButtons = deleteAccountModal.querySelectorAll('.close-modal');
    const deleteButtons = document.querySelectorAll('.delete-account-button');

    // Открытие модального окна и передача ID счета при нажатии на кнопку "Удалить"
    deleteButtons.forEach(function(button) {
        button.addEventListener('click', function() {
            deleteAccountModal.dataset.accountId = button.getAttribute('data-id').toString();
            deleteAccountModal.style.display = 'block';
        });
    });

    // Закрытие модального окна при нажатии на крестик
    closeButton.addEventListener('click', function() {
        deleteAccountModal.style.display = 'none';
    });

    // Закрытие модального окна при нажатии на кнопку "Нет"
    closeButtons.forEach(function(button) {
        button.addEventListener('click', function() {
            deleteAccountModal.style.display = 'none';
        });
    });

    // Отправка POST запроса при нажатии на кнопку "Да"
    confirmDeleteButton.addEventListener('click', function() {
        const accountId = deleteAccountModal.dataset.accountId;
        deleteAccount(accountId);
        deleteAccountModal.style.display = 'none';
    });
});

//Реализация открытия и закрытия окна с добавлением новой операции
document.addEventListener('DOMContentLoaded', function() {
    const createOperationModal = document.getElementById('create-operation-modal');
    const addOperationButton = document.getElementById('add-operation-button');
    const closeButton = createOperationModal.querySelector('.close');

    addOperationButton.addEventListener('click', function() {
        createOperationModal.style.display = 'block';
    });

    closeButton.addEventListener('click', function() {
        createOperationModal.style.display = 'none';
    });
});

// Добавления окна с подтверждением удаления операции
document.addEventListener('DOMContentLoaded', function() {
    const deleteOperationModal = document.getElementById('delete-operation-modal');
    const confirmDeleteButton = document.getElementById('confirm-delete-operation');
    const closeButton = deleteOperationModal.querySelector('.close');
    const closeButtons = deleteOperationModal.querySelectorAll('.close-modal');
    const deleteButtons = document.querySelectorAll('.delete-operation-button');

    // Открытие модального окна и передача ID счета при нажатии на кнопку "Удалить"
    deleteButtons.forEach(function(button) {
        button.addEventListener('click', function() {
            deleteOperationModal.dataset.operationId = button.getAttribute('data-id').toString();
            deleteOperationModal.style.display = 'block';
        });
    });

    // Закрытие модального окна при нажатии на крестик
    closeButton.addEventListener('click', function() {
        deleteOperationModal.style.display = 'none';
    });

    // Закрытие модального окна при нажатии на кнопку "Нет"
    closeButtons.forEach(function(button) {
        button.addEventListener('click', function() {
            deleteOperationModal.style.display = 'none';
        });
    });

    // Отправка POST запроса при нажатии на кнопку "Да"
    confirmDeleteButton.addEventListener('click', function() {
        const operationId = deleteOperationModal.dataset.operationId;
        deleteOperation(operationId);
        deleteOperationModal.style.display = 'none';
    });
});

let selectedOperationType = '';

function selectOperationType(type) {
    // Снимаем выделение с предыдущей выбранной кнопки
    const selectedButton = document.querySelector('.selected');
    if (selectedButton) {
        selectedButton.classList.remove('selected');
    }

    // Выделяем и делаем активной выбранную кнопку
    const button = event.target;
    button.classList.add('selected');

    // Обновляем выбранный тип операции
    selectedOperationType = type;
}

// Функция для создания новой операции
function createNewOperation() {
    const name = document.getElementById("new-operation-name").value;
    const amount = document.getElementById("new-operation-amount").value;
    const category = document.getElementById("new-operation-category").value;
    const accountId = document.getElementById("new-operation-account").value;

    if (!selectedOperationType) {
        alert('Пожалуйста, выберите тип операции (Доход или Расход)');
        return;
    }

    const operationRequest = {
        "name": name,
        "amount": amount,
        "category": category,
        "accountId": accountId,
        "type": selectedOperationType
    };

    fetch("/createOperation", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(operationRequest),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log('Успешно:', data);
            window.location.href = "http://localhost:8080/index";
        })
        .catch(error => {
            console.error('Ошибка:', error);
        });
}

//POST запрос на сервер для удаления счета
function deleteAccount(accountId) {
    fetch("/deleteAccount", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(accountId),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log('Успешно:', data);
            window.location.href = "http://localhost:8080/index";
        })
        .catch(error => {
            console.error('Ошибка:', error);
        });
}

//POST запрос на сервер для удаления счета
function deleteOperation(operationId) {
    fetch("/deleteOperation", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(operationId),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log('Успешно:', data);
            window.location.href = "http://localhost:8080/index";
        })
        .catch(error => {
            console.error('Ошибка:', error);
        });
}

//POST запрос на сервер для создания нового счета
function createNewAccount() {
    const name = document.getElementById("new-account-name").value;
    const amount = document.getElementById("new-account-amount").value;
    const currency = document.getElementById("new-account-currency").value;

    const accountRequest = {
        "name": name,
        "amount": amount,
        "currency": currency
    };

    fetch("/createAccount", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(accountRequest),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log('Успешно:', data);
            window.location.href = "http://localhost:8080/index";
        })
        .catch(error => {
            console.error('Ошибка:', error);
        });
}
