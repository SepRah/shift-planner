import {canManageStaffRoles, canManageUserRoles} from "../permissions/ManagementPermissions.js";
import {useAuth} from "../context/AuthContext.jsx";
import {Navigate, Outlet} from "react-router-dom";
import Navbar from "../components/Navbar.jsx";

export default function ManagementLayout() {
    const { user } = useAuth();

    // Anyone who can manage staff OR user roles may enter
    if (!user || (!canManageStaffRoles(user) && !canManageUserRoles(user))) {
        return <Navigate to="/unauthorized" replace />;
    }

    return (
        <>
            <Navbar />
            <div className="container mt-4">
                <h1>Management</h1>

                <Outlet />
            </div>
        </>
    );
}