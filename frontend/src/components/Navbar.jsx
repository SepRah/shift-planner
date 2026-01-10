import { Link,useNavigate } from "react-router-dom";
import "../styles/Navbar.css";
import { useAuth } from "../context/AuthContext";
import {canAccessManagementArea} from "../permissions/ManagementPermissions.js";
import FullLogoSvg from "../media/FullCalendarFullLogo.svg?react";

export default function Navbar() {
    const {user, logout} = useAuth();
    const navigate = useNavigate();

    const handleLogout =() => {
        logout();
        navigate("/login");
    }

    return (
        <nav className="navbar navbar-expand-lg navbar-dark">
            <div className="container-fluid">
                <Link className="navbar-link" to="/home" style={{ color: "white" }}>
                    <FullLogoSvg style={{ height: 60 }} />
                </Link>

                <ul className="navbar-nav">
                    <li className="nav-item">
                        <Link className="nav-link" to="/home">
                            Home
                        </Link>
                    </li>

                    <li className="nav-item">
                        <Link className="nav-link" to="/account">
                            Personal Data
                        </Link>
                    </li>

                    <li className="nav-item">
                        <Link className="nav-link" to="/planner">
                            Planner
                        </Link>
                    </li>

                    {canAccessManagementArea(user) && (
                        <li className="nav-item">
                            <Link className="nav-link" to="/management">
                                Management Area
                            </Link>
                        </li>
                    )}
                </ul>

                {/* Right side */}
                <div className="d-flex align-items-center gap-2">
                    {user?.username && (
                        <span style={{color: "white", opacity: 0.9}}>
                            {user.username}
                        </span>
                    )}

                    <button
                        type="button"
                        onClick={handleLogout}
                        className="btn btn-outline-light btn-sm"
                    >
                        Logout
                    </button>
                </div>
            </div>
        </nav>
    );
}
