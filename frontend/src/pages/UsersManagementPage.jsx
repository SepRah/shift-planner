import { useEffect, useState } from "react";
import UsersTable from "../components/UserTable.jsx";
import {
    activateUser,
    deactivateUser,
    fetchAllUsers,
    fetchAvailableRoles, fetchQualifications, updateFTE,
    updateStaffQualification,
    updateUserRoles
} from "../api/adminApi.js";
import {useAuth} from "../context/AuthContext.jsx";
import RoleModal from "../components/RoleModal.jsx";
import FTEModal from "../components/FTEModal.jsx";
import {canManageStaffRoles, canManageUserRoles} from "../permissions/ManagementPermissions.js";



export default function UsersManagementPage() {
    const { user: currentUser } = useAuth();

    const [users, setUsers] = useState([]);
    const [availableRoles, setAvailableRoles] = useState([]);

    const [availableQualifications, setAvailableQualifications] = useState([]);

    const [selectedUser, setSelectedUser] = useState(null);
    const [selectedRoles, setSelectedRoles] = useState([]);
    const [selectedQualification, setSelectedQualification] = useState([]);
    const [selectedStaffFTE, setSelectedStaffFTE] = useState(null);

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
        document.title = "Shiftplanner – Management Dashboard";
        loadUsers();
        // Get the qualifications
        fetchQualifications()
            .then(setAvailableQualifications)
            .catch(console.error);
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
                    setSelectedUser(user);
                    setSelectedQualification(user.staffQualificationLevel);
                }}
                onEditFte={(user) => {
                    setSelectedUser(user);
                    setSelectedStaffFTE(user.fte)
                }}
            />

            {/* Role Modal */}
            <RoleModal
                modalId="staffRoleModal"
                user={selectedUser}
                title="Update staff qualification"
                availableRoles={availableQualifications}
                selectedRoles={selectedQualification}
                selectionType="single"
                onSelectRole={setSelectedQualification}
                onSave={async () => {
                    await updateStaffQualification(
                        selectedUser,
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
            <FTEModal
                modalId="fteModal"
                user={selectedUser}
                title="Update user fte"
                value={selectedStaffFTE}
                onChange={setSelectedStaffFTE}
                onSave={async () => {
                    await updateFTE(
                        selectedUser,
                        selectedStaffFTE
                    );
                    loadUsers();
                }}
            />

        </>
    );
}