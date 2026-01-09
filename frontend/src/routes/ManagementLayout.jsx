import {canManageStaffRoles, canManageUserRoles} from "../permissions/ManagementPermissions.js";
import {useAuth} from "../context/AuthContext.jsx";
import {Navigate, NavLink, Outlet} from "react-router-dom";
import Navbar from "../components/Navbar.jsx";

export default function ManagementLayout() {
    const { user } = useAuth();

    // Anyone who can manage staff OR user roles may enter
    if (!user || (!canManageStaffRoles(user) && !canManageUserRoles(user))) {
        return <Navigate to="/unauthorized" replace />;
    }

    const ManagementTabs = () => (
        <ul className="nav nav-tabs mb-3">
            <li className="nav-item">
                <NavLink
                    to="users"
                    className={({ isActive }) =>
                        `nav-link ${isActive ? "active" : ""}`
                    }
                >
                    Users
                </NavLink>
            </li>
            <li className="nav-item">
                <NavLink
                    to="taskList"
                    className={({ isActive }) =>
                        `nav-link ${isActive ? "active" : ""}`
                    }
                >
                    Task list
                </NavLink>
            </li>
        </ul>
    );

    return (
        <>
            <Navbar />
            <div className="container mt-4">
                <h1>Management</h1>
                <ManagementTabs />
                <Outlet />
            </div>
        </>
    );
}