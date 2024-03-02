//Реализация диаграммы
document.addEventListener("DOMContentLoaded", function() {
    const operationAmounts = document.querySelectorAll('.transaction-amount');
    let totalIncome = 0;
    let totalExpense = 0;

    operationAmounts.forEach(function(operationInfo) {
        const amount = parseFloat(operationInfo.getAttribute('data-amount'));
        const type = operationInfo.getAttribute('data-type');

        if (type === 'income') {
            totalIncome += amount;
        } else {
            totalExpense += amount;
        }
    });

    const data = {
        labels: ['Доходы', 'Расходы'],
        datasets: [{
            label: 'Доходы и расходы',
            data: [totalIncome, totalExpense],
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
        const editButton = accountInfo.querySelector('.edit-account-button');

        accountInfo.addEventListener('mouseenter', function() {
            deleteButton.style.display = 'block';
            editButton.style.display = 'block';
        });

        accountInfo.addEventListener('mouseleave', function() {
            deleteButton.style.display = 'none';
            editButton.style.display = 'none';
        });
    });

    operationInfos.forEach(function(operationInfo) {
        const deleteButton = operationInfo.querySelector('.delete-operation-button');
        const editButton = operationInfo.querySelector('.edit-operation-button');

        operationInfo.addEventListener('mouseenter', function() {
            deleteButton.style.visibility = 'visible';
            editButton.style.visibility = 'visible';
        });

        operationInfo.addEventListener('mouseleave', function() {
            deleteButton.style.visibility = 'hidden';
            editButton.style.visibility = 'hidden';
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

//Реализация открытия и закрытия окна с редактированием операции
document.addEventListener('DOMContentLoaded', function() {
    const editOperationModal = document.getElementById('edit-operation-modal');
    const editButtonConfirm = document.getElementById('edit-operation-button');
    const editOperationButtons = document.querySelectorAll('.edit-operation-button');
    const closeButton = editOperationModal.querySelector('.close');

    editOperationButtons.forEach(button => {
        button.addEventListener('click', function() {
            editOperationModal.dataset.operationId = button.getAttribute('data-id').toString();
            editOperationModal.style.display = 'block';
        });
    });

    closeButton.addEventListener('click', function() {
        editOperationModal.style.display = 'none';
    });

    editButtonConfirm.addEventListener('click', function() {
        const operationId = editOperationModal.dataset.operationId;
        editOperation(operationId);
        editOperationModal.style.display = 'none';
    });
});

//Реализация открытия и закрытия окна с редактированием счета
document.addEventListener('DOMContentLoaded', function() {
    const editAccountModal = document.getElementById('edit-account-modal');
    const editButtonConfirm = document.getElementById('edit-account-button');
    const editAccountButtons = document.querySelectorAll('.edit-account-button');
    const closeButton = editAccountModal.querySelector('.close');

    editAccountButtons.forEach(button => {
        button.addEventListener('click', function() {
            editAccountModal.dataset.accountId = button.getAttribute('data-id').toString();
            editAccountModal.style.display = 'block';
        });
    });

    closeButton.addEventListener('click', function() {
        editAccountModal.style.display = 'none';
    });

    editButtonConfirm.addEventListener('click', function() {
        const accountId = editAccountModal.dataset.accountId;
        editAccount(accountId);
        editAccountModal.style.display = 'none';
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
            deleteOperationModal.dataset.operationAmount = button.getAttribute('data-amount').toString();
            deleteOperationModal.dataset.operationType = button.getAttribute('data-type').toString();
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
        const operationAmount = deleteOperationModal.dataset.operationAmount;
        const operationType = deleteOperationModal.dataset.operationType;
        deleteOperation(operationId, operationAmount, operationType);
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
    const date = document.getElementById("new-operation-date").value;

    console.log(date)

    if (!selectedOperationType) {
        alert('Пожалуйста, выберите тип операции (Доход или Расход)');
        return;
    }

    const operationRequest = {
        "name": name,
        "amount": amount,
        "category": category,
        "accountId": accountId,
        "date": date,
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

let selectedOperationTypeForEdit = '';

function selectOperationTypeForEdit(type) {
    // Снимаем выделение с предыдущей выбранной кнопки
    const selectedButton = document.querySelector('.selected');
    if (selectedButton) {
        selectedButton.classList.remove('selected');
    }

    // Выделяем и делаем активной выбранную кнопку
    const button = event.target;
    button.classList.add('selected');

    // Обновляем выбранный тип операции
    selectedOperationTypeForEdit = type;
}

// Функция для создания новой операции
function editOperation(operationId) {
    const name = document.getElementById("edit-operation-name").value;
    const amount = document.getElementById("edit-operation-amount").value;
    const category = document.getElementById("edit-operation-category").value;
    const accountId = document.getElementById("edit-operation-account").value;

    if (!selectedOperationTypeForEdit) {
        alert('Пожалуйста, выберите тип операции (Доход или Расход)');
        return;
    }

    const operationRequest = {
        "operationId": operationId,
        "name": name,
        "amount": amount,
        "category": category,
        "accountId": accountId,
        "type": selectedOperationTypeForEdit
    };

    fetch("/editOperation", {
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
function deleteOperation(operationId, operationAmount, operationType) {
    const deleteRequest = {
        "id": operationId,
        "amount": operationAmount,
        "type": operationType
    };

    fetch("/deleteOperation", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(deleteRequest),
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


//POST запрос на сервер для редактирования счета
function editAccount(accountId) {
    const name = document.getElementById("edit-account-name").value;
    const amount = document.getElementById("edit-account-amount").value;
    const currency = document.getElementById("edit-account-currency").value;

    const accountRequest = {
        "accountId": accountId,
        "name": name,
        "amount": amount,
        "currency": currency
    };

    fetch("/editAccount", {
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