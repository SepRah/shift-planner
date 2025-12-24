import jwtDecode from "jwt-decode";

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

export function getUserRoles() {
    const decoded = decodeToken();
    return decoded?.roles ?? [];
}