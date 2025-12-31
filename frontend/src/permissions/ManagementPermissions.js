export const canManageUserRoles = (user) =>
    user.roles.includes("ADMIN") || user.roles.includes("SYSTEM_ADMIN");

export const canManageStaffRoles = (user) =>
    user.staff.includes("MANAGER") ||
    user.staff.includes("SENIOR") ||
    canManageUserRoles(user);