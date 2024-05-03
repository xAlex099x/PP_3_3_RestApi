$(async function () {
    await getUsersTable();
})
const userFetchService = {
    head: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'X-XSRF-TOKEN': getCsrfToken(),
        'Referer': null
    },
    getPeople: async () => await fetch('/rest_api/get_people'),
    addNewUser: async (user) => await fetch('/rest_api/new', {
        method: 'POST',
        headers: userFetchService.head,
        body: JSON.stringify(user)
    }),
    editUser: async (user) => await fetch(`/rest_api/edit`, {
        method: 'POST',
        headers: userFetchService.head,
        body: JSON.stringify(user)
    }),
    deleteUser: async (id) => {
        const bodyData = JSON.stringify({id: id});  // Подготовка данных для отправки
        return await fetch(`/rest_api/delete/`, {
            method: 'POST',
            headers: userFetchService.head,
            body: bodyData
        });
    }
}

//Listeners
document.addEventListener('DOMContentLoaded', function () {
    $("#usersTable").on('click', 'button', handleTableButtonClick);
});
document.addEventListener('DOMContentLoaded', function () {
    var editForm = document.getElementById('editUserForm');
    if (editForm) {
        editForm.addEventListener('submit', submitEditUserForm);
    }
});
document.addEventListener('DOMContentLoaded', function () {
    var form = document.getElementById('createUserForm');
    form.addEventListener('submit', submitNewUserForm);
});
document.addEventListener('DOMContentLoaded', function () {
    var form = document.getElementById('deleteUserForm');
    form.addEventListener('submit', submitDeleteUserForm);
});

//Get users table
async function getUsersTable() {
    let table = $('#usersTable tbody');
    table.empty();

    await userFetchService.getPeople()
        .then(res => res.json())
        .then(users => {
            users.forEach(user => {
                let tableFilling = `$(
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.username}</td>
                            <td>${user.email}</td>
                            <td>${user.roles}</td>
                            <td>
                                <button type="button" data-userid="${user.id}" data-action="edit" class="btn btn-primary">Edit</button>    
                            </td>
                            <td>
                                <button type="button" data-userid="${user.id}" data-action="delete" class="btn btn-danger">Delete</button>
                            </td>     
                        </tr>
                )`;
                table.append(tableFilling);
            })
        });
}

//New user
function submitNewUserForm(event) {
    event.preventDefault();
    var form = event.target;
    var formData = new FormData(form);

    var user = convertFormDataToUserObject(formData, 'new_user_');
    console.log("Sending JSON:", JSON.stringify(user));

    userFetchService.addNewUser(user)
        .then(response => {
            if (!response.ok) {
                return response.json().then(data => {
                    throw data;
                });
            }
            return response.json();
        })
        .then(data => {
            if (data.status && data.status !== 200) {
                handleValidationErrors(data, 'new_user');
            } else {
                $('#createUserModal').modal('hide');
                getUsersTable();
            }
        })
        .catch(error => {
            handleValidationErrors(error, 'new_user');
        });
}

//Edit user
function submitEditUserForm(event) {
    event.preventDefault();
    var formData = new FormData(event.target);
    var user = convertFormDataToUserObject(formData, 'edit_user_');
    console.log("Sending JSON:", JSON.stringify(user));

    userFetchService.editUser(user) // Напрямую вызываем сервис для редактирования пользователя
        .then(response => {
            if (!response.ok) {
                // Если ответ сервера не успешен, получаем тело ответа и обрабатываем ошибки
                return response.json().then(body => {
                    throw body; // Бросаем объект с ошибками
                });
            }
            return response.json(); // Получаем данные пользователя в случае успеха
        })
        .then(data => {
            // Здесь обрабатываем успешный ответ
            $('#editUserModal').modal('hide');
            location.reload();
        })
        .catch(error => {
            // Обработка ошибок валидации или других ошибок
            handleValidationErrors(error, 'edit_user');
        });
}

//Delete user
function submitDeleteUserForm(event) {
    event.preventDefault();
    var userId = $('#delete_user_id').val();

    userFetchService.deleteUser(userId)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log('Success:', data);
            $('#deleteUserModal').modal('hide');
            location.reload();
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

//Utility functions
function handleTableButtonClick(event) {
    let targetButton = $(event.target);
    let buttonUserId = targetButton.attr('data-userid');
    let buttonAction = targetButton.attr('data-action');
    let userRow = targetButton.closest("tr");

    if (buttonAction === "edit") {
        let editModal = $('#editUserModal');
        editModal.find('#edit_user_id').val(userRow.find('td:nth-child(1)').text());
        editModal.find('#edit_user_username').val(userRow.find('td:nth-child(2)').text());
        editModal.find('#edit_user_email').val(userRow.find('td:nth-child(3)').text());
        editModal.modal('show');
    } else if (buttonAction === "delete") {
        let deleteModal = $('#deleteUserModal');
        deleteModal.find('#delete_user_id').val(userRow.find('td:nth-child(1)').text());
        deleteModal.find('#delete_user_username').val(userRow.find('td:nth-child(2)').text());
        deleteModal.find('#delete_user_email').val(userRow.find('td:nth-child(3)').text());
        deleteModal.modal('show');
    }
}

function convertFormDataToUserObject(formData, prefixToRemove) {
    var user = {};
    formData.forEach((value, key) => {
        if (key.startsWith(prefixToRemove)) {
            let newKey = key.slice(prefixToRemove.length);
            if (newKey.startsWith('_')) {
                newKey = newKey.slice(1);
            }
            user[newKey] = (newKey === 'roles') ? [value] : value;
        }
    });
    return user;
}

function handleValidationErrors(errors, prefixToAdd) {
    $('.error-message').remove();
    $('.is-invalid').removeClass('is-invalid');

    Object.entries(errors).forEach(([key, message]) => {
        var inputField = $('#' + prefixToAdd + '_' + key);
        console.log('#' + prefixToAdd + '_' + key);
        inputField.addClass('is-invalid');
        inputField.after(`<span class="error-message" style="color:red;">${message}</span>`);
    });
}

function getCsrfToken() {
    let csrfToken = "";
    const cookies = document.cookie.split(';');
    for (let cookie of cookies) {
        if (cookie.trim().startsWith("XSRF-TOKEN=")) {
            csrfToken = decodeURIComponent(cookie.trim().substring("XSRF-TOKEN=".length));
            break;
        }
    }
    return csrfToken;
}