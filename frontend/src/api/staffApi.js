import api from "./axios";

export const getStaffMembers = () =>
  api.get("/api/staffmembers").then(res => res.data);