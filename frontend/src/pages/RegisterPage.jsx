import { Link } from "react-router-dom";
import { useState, useEffect } from "react";
import {FaEye, FaEyeSlash} from "react-icons/fa";
import PasswordInput from "../components/PasswordInput.jsx";

export default function RegisterPage() {

    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [username, setUsername] = useState("");
    const [usernameTouched, setUsernameTouched] = useState(false);
    const [password, setPassword] = useState("");
    const [showPassword, setShowPassword] = useState(false);
    const [confirmPassword, setConfirmPassword] = useState("");
    const [showConfirmPassword, setShowConfirmPW] = useState(false);
    // Set password rules
    const [passwordTouched, setPasswordTouched] = useState(false);
    const [confirmTouched, setConfirmTouched] = useState(false);
    const passwordsMatch = password === confirmPassword;
    const passwordTooShort = password.length > 0 && password.length < 8;

    // Create an example Username out of the First- and lastname
    useEffect(() => {
        if (usernameTouched) return;

        const generatedUsername =
            lastName.slice(0, 3).toLowerCase() +
            firstName.slice(0, 2).toLowerCase();

        setUsername(generatedUsername);
    }, [firstName, lastName, usernameTouched]);


    const handleSubmit = (e) => {
        e.preventDefault();

        console.log("Register:", username, password, confirmPassword);
        // API logic comes later
    };

    return (
        <div className="d-flex justify-content-center align-items-center vh-100 bg-light">
            <div className="card shadow p-4" style={{ maxWidth: "420px", width: "100%" }}>
                <h2 className="text-center mb-3">Register</h2>
                <p className="text-center text-muted mb-4">
                    Create a new Shiftplanner account
                </p>

                <form onSubmit={handleSubmit}>
                    {/* Firstname */}
                    <div className="mb-3">
                        <label className="form-label">Firstname</label>
                        <input
                            type="text"
                            className="form-control"
                            placeholder="Firstname"
                            value={firstName}
                            onChange={(e) => setFirstName(e.target.value)}
                            autoComplete="firstname"
                            required
                        />
                    </div>

                    {/* Lastname */}
                    <div className="mb-3">
                        <label className="form-label">Lastname</label>
                        <input
                            type="text"
                            className="form-control"
                            placeholder="Lastname"
                            value={lastName}
                            onChange={(e) => setLastName(e.target.value)}
                            autoComplete="lastname"
                            required
                        />
                    </div>

                    {/* Username */}
                    <div className="mb-3">
                        <label className="form-label">Username</label>
                        <input
                            type="text"
                            className="form-control"
                            placeholder="Choose a username"
                            value={username}
                            onChange={(e) => {
                                setUsernameTouched(true);
                                setUsername(e.target.value);
                            }}
                            autoComplete="username"
                            required
                        />
                    </div>

                    {/* Password */}
                    <PasswordInput
                        label="Password"
                        placeholder="Choose a password"
                        value={password}
                        onChange={(e) => {
                            setPassword(e.target.value);
                            setPasswordTouched(true);
                        }}
                        isInvalid={passwordTouched && passwordTooShort}
                        errorMessage="Password must be at least 8 characters long"
                    />

                    {/* Confirm Password */}
                    <PasswordInput
                        label="Confirm password"
                        placeholder="Repeat your password"
                        value={confirmPassword}
                        onChange={(e) => {
                            setConfirmPassword(e.target.value);
                            setConfirmTouched(true);
                        }}
                        isInvalid={confirmTouched && !passwordsMatch}
                        errorMessage="Passwords do not match!"
                    />

                    {/* Register Button */}
                    <button type="submit" className="btn btn-primary w-100">
                        Register
                    </button>
                </form>

                <p className="text-center mt-3">
                    Already have an account?{" "}
                    <Link to="/login" className="text-decoration-none">
                        Back to login
                    </Link>
                </p>
            </div>
        </div>
    );
}
