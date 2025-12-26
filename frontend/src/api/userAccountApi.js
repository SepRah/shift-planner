import api from "./axios.js";

/**
 * Get the user information
 * @return user information data
 */
export async function getMe(){
    const response = await api.get(`/api/users/me`);
    return response.data;
}

/**
 * Changes the password of the user
 * @param oldPassword the old Password
 * @param newPassword the new password
 * @return the  data
 */
export async function changePassword(oldPassword, newPassword){
    const response = await api.post("/api/change-password", {
        oldPassword: oldPassword,
        newPassword: newPassword
    });
    return response.data;
}