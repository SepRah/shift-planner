import { Link } from "react-router-dom";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import PasswordInput from "../components/PasswordInput.jsx";
import {register} from "../services/authService.js";
import FullLogo from "../media/FullCalendarFullLogo.svg";

export default function RegisterPage() {

    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [username, setUsername] = useState("");
    const [usernameTouched, setUsernameTouched] = useState(false);

    const [fte, setFTE] = useState(1.0);
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    // Set password rules
    const [passwordTouched, setPasswordTouched] = useState(false);
    const [confirmTouched, setConfirmTouched] = useState(false);
    const passwordsMatch = password === confirmPassword;
    const passwordTooShort = password.length > 0 && password.length < 8;

    const [error, setError] = useState(null);

    // Create an example Username out of the First- and lastname
    useEffect(() => {
        document.title = "Shiftplanner – Register";
        if (usernameTouched) return;

        const generatedUsername =
            lastName.slice(0, 3).toLowerCase() +
            firstName.slice(0, 2).toLowerCase();

        setUsername(generatedUsername);
    }, [firstName, lastName, usernameTouched]);

    // init navigation function
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            // Try to register
            await register({username, password, confirmPassword, firstName, lastName, fte});
            setError(null);
            navigate("/register/success");
        } catch (err) {
            const message =
                err.response?.data?.message || "Registration failed";
            // Give back the error message
            setError(message);
        }

    };

    return (
        <div className="d-flex justify-content-center align-items-center vh-100 bg-light">
            <div className="card shadow p-4" style={{ maxWidth: "420px", width: "100%" }}>
                <img src={FullLogo} height={130} alt="ShiftPlanner" />
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

                    {/* FTE */}
                    <div className="mb-3">
                        <label className="form-label">FTE</label>
                        <input
                            type="number"
                            className="form-control"
                            placeholder="Set an FTE"
                            value={fte}
                            min="0"
                            max="1"
                            step="0.1"
                            onChange={(e) => setFTE(Number(e.target.value))}
                            autoComplete="off"
                            required
                        />
                    </div>

                    {/* Password */}
                    <PasswordInput
                        id="Password"
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
                        id="Confirm password"
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
                    <button
                        type="submit"
                        className={
                            `btn btn-primary w-100 ${!passwordsMatch || passwordTooShort ? "disabled" : ""}`}>
                        Register
                    </button>
                </form>

                {error && (
                    <div className="alert alert-danger mt-3">
                        {error}
                    </div>
                )}

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
