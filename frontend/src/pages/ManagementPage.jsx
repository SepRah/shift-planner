import { useEffect, useState } from "react";
import UsersTable from "../components/UserTable.jsx";
import {
    activateUser,
    deactivateUser,
    fetchAllUsers,
    fetchAvailableRoles,
    updateStaffQualification,
    updateUserRoles
} from "../api/adminApi.js";
import {useAuth} from "../context/AuthContext.jsx";
import RoleModal from "../components/RoleModal.jsx";
import {canManageStaffRoles, canManageUserRoles} from "../permissions/ManagementPermissions.js";



export default function ManagementPage() {
    const { user: currentUser } = useAuth();

    const [users, setUsers] = useState([]);
    const [availableRoles, setAvailableRoles] = useState([]);

    const [selectedUser, setSelectedUser] = useState(null);
    const [selectedRoles, setSelectedRoles] = useState([]);

    const [selectedStaffUser, setSelectedStaffUser] = useState(null);
    const [selectedQualification, setSelectedQualification] = useState([]);

    /**
     * Loads all users for the admin dashboard.
     *
     * Fetches the user list from the admin API and updates
     * the local component state used for rendering.
     */
    async function loadUsers() {
        const data = await fetchAllUsers();
        setUsers(data);
    }

    useEffect(() => {
        document.title = "Shiftplanner – Admin Dashboard";
        loadUsers();
    }, []);

    /**
     * Sets a user active or deactivated
     * @param user the user to be activated/deactivated
     */
    async function toggleActive(user) {
        if (user.active) {
            await deactivateUser(user.id);
        } else {
            await activateUser(user.id);
        }
        loadUsers();
    }

    /**
     *
     * @param userID The unique User id
     * @return A List of the available roles
     */
    async function getAvailableRoles(userID){
        fetchAvailableRoles(userID).then(setAvailableRoles);
    }

    /**
     * Toggles the roles
     * @param role role to toggle
     */
    function toggleRole(role) {
        setSelectedRoles(prev =>
            prev.includes(role)
                ? prev.filter(r => r !== role)
                : [...prev, role]
        );
    }

    /**
     * Saves the roles
     */
    async function saveRoles() {
        await updateUserRoles(selectedUser.id, selectedRoles);
        loadUsers();
    }


    return (
        <>
            <UsersTable
                users={users}
                onToggleActive={toggleActive}
                onEditRoles={(user) => {
                    setSelectedUser(user);
                    setSelectedRoles(user.roles);
                    getAvailableRoles(user.id);
                }}
                canEditUserRoles={
                    canManageUserRoles(currentUser)
                }
                canEditStaffQualification={canManageStaffRoles(currentUser)}
                onEditStaffQualification={(user) => {
                    setSelectedStaffUser(user);
                    setSelectedQualification(user.staffQualificationLevel);
                }}
            />

            {/* Role Modal */}
            <RoleModal
                modalId="staffRoleModal"
                user={selectedStaffUser}
                title="Update staff qualification"
                availableRoles={["JUNIOR", "SENIOR", "MANAGER"]}
                selectedRoles={selectedQualification}
                selectionType="single"
                onSelectRole={setSelectedQualification}
                onSave={async () => {
                    await updateStaffQualification(
                        selectedStaffUser.id,
                        selectedQualification
                    );
                    loadUsers();
                }}
            />

            <RoleModal
                modalId="userRoleModal"
                user={selectedUser}
                title="Update user roles"
                availableRoles={availableRoles}
                selectedRoles={selectedRoles}
                selectionType="multiple"
                onToggleRole={toggleRole}
                onSave={saveRoles}
            />
        </>
    );
}