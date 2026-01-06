import api from "./axios.js";

/**
 * Creates a new task assignment via the API.
 * @param {Object} data - The task assignment data to create
 * @returns {Promise<Object>} The created task assignment
 */
export const createTaskAssignment = (data) =>
    api.post("/api/task-assignments", data).then(res => res.data);

/**
 * Updates an existing task assignment via the API.
 * @param {number|string} id - The ID of the task assignment to update
 * @param {Object} data - The updated task assignment data
 * @returns {Promise<Object>} The updated task assignment
 */
export const updateTaskAssignment = (id, data) =>
    api.put(`/api/task-assignments/${id}`, data).then(res => res.data);

export const getTasks = () => api.get("/api/tasks").then(res => res.data);
export const getAllTasksInclInactive = () => api.get("/api/tasks/all").then(res => res.data);

/**
 * Creates a new task via the API.
 * @param {Object} task - The task object to create
 * @returns {Promise<Object>} The created task
 */
export const createTask = (task) => api.post("/api/tasks", task).then(res => res.data);

/**
 * Updates an existing task via the API.
 * @param {number|string} id - The ID of the task to update
 * @param {Object} task - The updated task object
 * @returns {Promise<Object>} The updated task
 */
export const updateTask = (id, task) => api.put(`/api/tasks/${id}`, task).then(res => res.data);
export const deleteTask = (id) => api.delete(`/api/tasks/${id}`).then(res => res.data);

/**
 * Fetches all qualification levels from the API.
 * @returns {Promise<Array>} List of qualification levels
 */
export const getQualificationLevels = () =>
    api.get("/api/qualification-levels").then(res => res.data);

/**
 * Fetches all task assignments from the API.
 * @returns {Promise<Array>} List of all task assignments
 */
export async function fetchAllTaskAssignments() {
    const response = await api.get("/api/task-assignments");
    return response.data;
}

/**
 * Fetches all task assignments for a specific staff member.
 * @param {number|string} staffId - The ID of the staff member
 * @returns {Promise<Array>} List of task assignments for the staff member
 */
export async function fetchTaskAssignmentsByStaffId(staffId) {
    try {
        const response = await api.get(`/api/task-assignments/staff/${staffId}`);
        return response.data;
    } catch (error) {
        console.error("Error while fetching task assignments for staffId:", staffId, error);
        throw new Error(error.response?.data?.message ?? "Failed to load task assignments");
    }
}