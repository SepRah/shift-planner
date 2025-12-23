import { Link } from "react-router-dom";
import "../styles/Navbar.css";

export default function Navbar() {
    return (
        <nav className="navbar navbar-expand-lg navbar-dark">
            <div className="container-fluid">
                <Link className="navbar-brand" to="/">
                    ShiftPlanner
                </Link>

                <ul className="navbar-nav">
                    <li className="nav-item">
                        <Link className="nav-link" to="/home">
                            Personal Data
                        </Link>
                    </li>

                    <li className="nav-item">
                        <Link className="nav-link" to="/planner">
                            Planner
                        </Link>
                    </li>

                </ul>
            </div>
        </nav>
    );
}
