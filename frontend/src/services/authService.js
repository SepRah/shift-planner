import api from "../api/axios";

export async function register({ username, password, confirmPassword, firstName, lastName, fte }) {
    if (password !== confirmPassword) {
        throw new Error("Passwords do not match");
    }

    if (password.length < 8) {
        throw new Error("Password too short");
    }

    const response = await api.post("/auth/register", {
        username,
        password,
        firstName,
        lastName,
        qualification: "NONE",
        fte
    });

    return response.data;
}

export async function login({ username, password}) {

    const response = await api.post("/auth/login", {
        username,
        password,
    });

    return response.data;
}

export function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    window.location.href = "/login";
}