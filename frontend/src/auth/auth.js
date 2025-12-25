import { jwtDecode } from "jwt-decode";

/**
 * Decodes the jwt token.
 *
 * Fetches the token from the local storage and decodes it.
 */
export function decodeToken() {
    const token = localStorage.getItem("token");

    if (!token) {
        return null;
    }

    try {
        return jwtDecode(token);
    } catch (err) {
        console.error("Invalid JWT", err);
        return null;
    }
}

/**
 * Returns the user roles.
 *
 * From the decoded jwt token.
 */
export function getUserRoles() {
    const decoded = decodeToken();
    return decoded?.roles ?? [];
}