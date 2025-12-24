import api from "./axios.js";

export async function fetchAdminUsers() {
    const response = await api.get("/admin/users");
    return response.data;
}

export async function deactivateUser(userId) {
    await api.put(`/admin/users/${userId}/deactivate`);
}

export async function activateUser(userId) {
    await api.put(`/admin/users/${userId}/activate`);
}