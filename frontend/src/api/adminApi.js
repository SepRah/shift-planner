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
 * @param {int} userId the unique user ID
 */
export async function deactivateUser(userId) {
    await api.put(`/api/admin/users/${userId}/deactivate`);
}

/**
 * Activates a user via PUT request
 * @param {int} userId the unique user ID
 */
export async function activateUser(userId) {
    await api.put(`/api/admin/users/${userId}/activate`);
}

/**
 * Updates the user roles
 * @param {int} userId the unique user ID
 * @param {Set<"ADMIN"|"SYSTEM_ADMIN"|"USER">} roles Set of roles
 */
export async function updateUserRoles(userId, roles) {
    await api.put(`/api/admin/users/${userId}/roles`, {
        roles: roles
    });
}

/**
 * Fetches the user available roles from the backend
 * @param {int} userId the unique user ID
 * @return The roles
 */
export async function fetchAvailableRoles(userId) {
    const response = await api.get(`/api/admin/users/${userId}/assignable-roles`);
    return response.data;
}

export async function updateStaffQualification(staffID, roles){

}


/**
 * Changes the FTE of the user
 * @param {number} staffId
 * @param {number} newFte
 */
// export async function changeFTE({ staffId, newFte }) {
//     const response = await api.post("/api/users/change-fte", {
//         id: staffId,
//         fte: newFte
//     });
//     return response.data;
// }