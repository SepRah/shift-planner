import {useEffect, useState} from "react";
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import { FaEye, FaEyeSlash } from "react-icons/fa";
import {login as loginAPI} from "../services/authService.js";
import {useAuth} from "../context/AuthContext.jsx";
import FullLogo from "../media/FullCalendarFullLogo.svg";

export default function LoginPage() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [showPassword, setShowPassword] = useState(false);
    const [error, setError] = useState(null);
    const [hasError, setHasError] = useState(false);

    // Adjust page title
    useEffect(() => {
        document.title = "Shiftplanner – Login";
    })

    // init navigation function
    const navigate = useNavigate();
    const { login } = useAuth();

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const data = await loginAPI({username, password});
            // Save the token
            login(data.token);

            setError(null);
            setHasError(false);
            // Redirect to homepage
            navigate("/home");

        } catch (err) {
            if (err.response?.status === 401) {
                setError("Invalid username or password");
                setHasError(true);
            } else {
                setError("Something went wrong. Please try again.");
            }
        }
    };

    return (
        <div className="d-flex justify-content-center align-items-center vh-100 bg-light">
            <div className="card shadow p-4" style={{maxWidth: "400px", width: "100%"}}>
                <img src={FullLogo} height={130} alt="ShiftPlanner" />
                <h2 className="text-center mb-3">Login</h2>
                <p className="text-center text-muted mb-4">
                    Sign in to your Shiftplanner account
                </p>

                <form onSubmit={handleSubmit}>
                    {/* Username */}
                    <div className="mb-3">
                        <label className="form-label">Username</label>
                        <input
                            type="text"
                            className={`form-control ${hasError ? "is-invalid" : ""}`}
                            placeholder="Enter username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />
                    </div>

                    {/* Password */}
                    <div className="mb-3">
                        <label className="form-label">Password</label>
                        <div className="input-group">
                            <input
                                type={showPassword ? "text" : "password"}
                                className={`form-control ${hasError ? "is-invalid" : ""}`}
                                placeholder="Enter password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />

                            <button
                                type="button"
                                className="btn btn-outline-secondary"
                                onClick={() => setShowPassword(!showPassword)}
                            >
                                {showPassword ? <FaEyeSlash/> : <FaEye/>}
                            </button>
                        </div>
                    </div>

                    {/* Login Button */}
                    <button type="submit" className="btn btn-primary w-100">
                        Login
                    </button>
                </form>

                {error && (
                    <div className="alert alert-danger mt-2" role="alert">
                        {error}
                    </div>
                )}

                <p className="text-center mt-3">
                    Don’t have an account?{" "}
                    <Link to="/register" className="text-decoration-none">
                        Register here
                    </Link>
                </p>
            </div>
        </div>
    );
}
