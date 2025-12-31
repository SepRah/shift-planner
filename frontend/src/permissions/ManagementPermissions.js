export const canManageUserRoles = (user) =>
    user.roles.includes("ADMIN") || user.roles.includes("SYSTEM_ADMIN");

export const canManageStaffRoles = (user) =>
    user.staff.includes("MANAGER") ||
    user.staff.includes("SENIOR") ||
    canManageUserRoles(user);

export const canAccessManagementArea = (user) =>
    user?.roles?.some(role =>
        role === "ADMIN" || role === "SYSTEM_ADMIN"
    ) ||
    ["MANAGER", "SENIOR"].includes(user?.staff);