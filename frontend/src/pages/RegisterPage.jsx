import { Link } from "react-router-dom";

export default function RegisterPage() {
    const handleSubmit = (e) => {
        e.preventDefault();
        // later you'll call your registration API here
    };

    return (
        <div className="auth-page">
            <div className="auth-card">
                <h1>Register</h1>
                <p className="auth-subtitle">Create a new Shiftplanner account</p>

                <form onSubmit={handleSubmit} className="auth-form">
                    <div className="form-group">
                        <label htmlFor="username">Username</label>
                        <input
                            id="username"
                            name="username"
                            type="text"
                            autoComplete="username"
                            placeholder="Choose a username"
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="password">Password</label>
                        <input
                            id="password"
                            name="password"
                            type="password"
                            autoComplete="new-password"
                            placeholder="Choose a password"
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="confirmPassword">Confirm password</label>
                        <input
                            id="confirmPassword"
                            name="confirmPassword"
                            type="password"
                            autoComplete="new-password"
                            placeholder="Repeat your password"
                            required
                        />
                    </div>

                    <button type="submit" className="auth-button">
                        Register
                    </button>
                </form>

                <p className="auth-footer">
                    Already have an account?{" "}
                    <Link to="/login">Back to login</Link>
                </p>
            </div>
        </div>
    );
}
