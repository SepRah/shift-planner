import { useState } from "react";
import { Link } from "react-router-dom";
import { FaEye, FaEyeSlash } from "react-icons/fa";

export default function LoginPage() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [showPassword, setShowPassword] = useState(false);

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log("Login:", username, password);
        // API logic comes later
    };

    return (
        <div className="d-flex justify-content-center align-items-center vh-100 bg-light">
            <div className="card shadow p-4" style={{ maxWidth: "400px", width: "100%" }}>
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
                            className="form-control"
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
                                className="form-control"
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
                                {showPassword ? <FaEyeSlash /> : <FaEye />}
                            </button>
                        </div>
                    </div>

                    {/* Login Button */}
                    <button type="submit" className="btn btn-primary w-100">
                        Login
                    </button>
                </form>

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
