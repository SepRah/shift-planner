import api from "./axios.js";

/**
 * Fetches all users.
 *
 * Fetches the user list from the admin API and updates
 * the local component state used for rendering.
 */
export async function fetchAllUsers() {
    const response = await api.get("/api/admin/users");
    return response.data;
}

/**
 * Deactivates a user via PUT request
 */
export async function deactivateUser(userId) {
    await api.put(`/api/admin/users/${userId}/deactivate`);
}

/**
 * Activates a user via PUT request
 */
export async function activateUser(userId) {
    await api.put(`/api/admin/users/${userId}/activate`);
}

/**
 * Updates the user roles
 */
export async function updateUserRoles(userId, roles) {
    console.log(roles)
    await api.put(`/api/admin/users/${userId}/roles`, {
        roles: roles
    });
}