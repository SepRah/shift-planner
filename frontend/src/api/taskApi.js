import api from "./axios.js";

/**
 * Fetches all task assignments.
 *
 * Fetches the all task-assignments  from the task-assignment API and updates
 * the local component state used for rendering.
 */
export async function fetchAllTaskAssignments() {
    const response = await api.get("/api/task-assignments");
    return response.data;
}


export async function fetchTaskAssignmentsByStaffId(staffId) {
    try {
        const response = await api.get(`/api/task-assignments/staff/${staffId}`);
        return response.data;
    } catch (error) {
        console.error(
            "Error while fetching task assignments for staffId:",
            staffId,
            error
        );

        throw new Error(
            error.response?.data?.message??"Failed to load task assignments"
        );
    }
}