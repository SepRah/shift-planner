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
 * Returns the correct qualification for depiction
 * @param {"NONE" | "MANAGER" | "SENIOR" | "JUNIOR"} rawQualification The raw user Qualification
 */
export function getAdjustedQualification(rawQualification){
    switch (rawQualification) {
        case "MANAGER":
            return "Manager";
        case "SENIOR":
            return "Senior Staff";
        case "JUNIOR":
            return "Junior Staff";
        default:
            return "No qualification";
    }
}
/**
 * Changes the password of the user
 * @param {string} oldPassword the old Password
 * @param {string} newPassword the new password
 * @return the  data
 */
export async function changePassword(oldPassword, newPassword){

    if (newPassword.length < 8) {
        throw new Error("Password too short");
    }

    const response = await api.post("/api/users/change-password", {
        oldPassword: oldPassword,
        newPassword: newPassword
    });
    return response.data;
}
