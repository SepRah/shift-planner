export async function register({ username, password, confirmPassword }) {
    if (password !== confirmPassword) {
        throw new Error("Passwords do not match");
    }

    if (password.length < 8) {
        throw new Error("Password too short");
    }

    return fetch("/api/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password }),
    });
}
