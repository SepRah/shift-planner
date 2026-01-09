import {useEffect, useState} from "react";
import Navbar from "../components/Navbar";
import {changePassword, getAdjustedQualification, getMe} from "../api/userAccountApi.js";
import LoadingSpinner from "../components/LoadingSpinner.jsx";
import PasswordInput from "../components/PasswordInput.jsx";

/**
 * Creates the user dashboard page
 * @return {JSX.Element}
 * @constructor
 */
export default function UserDashboard(){

    const [profile, setProfile] = useState(null);
    const [oldPassword, setOldPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        document.title = "Shiftplanner – User Dashboard";
        // Get the user data
        const loadUserData = async () => {
            try {
                const res = await getMe();
                setProfile(res);
            } catch (err) {
                console.error("Failed to load profile", err);
            }
        };

        loadUserData();
    }, []);

    const handleChangePassword = async () => {
        try {
            setLoading(true);
            setError(null);
            setSuccess(false);

            await changePassword(oldPassword, newPassword);
            // Set success to true
            setSuccess(true);
            // reset fields
            setOldPassword("");
            setNewPassword("");

        } catch (err) {
            setError(
                err.response?.data?.message ??
                "Failed to change password"
            );
        } finally {
            setLoading(false);
        }
    };


    if (!profile) {
        return (
            <>
                <Navbar />
                <div className="container mt-4">
                    <LoadingSpinner text="Loading profile…" />
                </div>
            </>
        );
    }

    return (
        <>
            <Navbar />
            <div className="container mt-4">
                <h2>User Dashboard</h2>

                <div className="row mt-4">
                    {/* LEFT COLUMN */}
                    <div className="col-md-8">
                        <div className="card">
                            <div className="card-header">
                                Personal Information
                            </div>
                            <div className="card-body">

                                {/* Name */}
                                <div className="row mb-3">
                                    <div className="col-md-6">
                                        <label htmlFor="firstName" className="form-label">
                                            First name
                                        </label>
                                        <input
                                            id="firstName"
                                            type="text"
                                            className="form-control"
                                            value={profile.firstName ?? ""}
                                            readOnly
                                            disabled
                                        />
                                    </div>

                                    <div className="col-md-6">
                                        <label htmlFor="lastName" className="form-label">
                                            Last name
                                        </label>
                                        <input
                                            id="lastName"
                                            type="text"
                                            className="form-control"
                                            value={profile.lastName ?? ""}
                                            readOnly
                                            disabled
                                        />
                                    </div>
                                </div>

                                {/* Staff related */}
                                <div className="row mb-3">
                                    <div className="col-md-6">
                                        <label htmlFor="fte" className="form-label">
                                            FTE
                                        </label>
                                        <input
                                            id="fte"
                                            type="number"
                                            className="form-control"
                                            value={profile.fte ?? ""}
                                            readOnly
                                            disabled
                                        />
                                    </div>

                                    <div className="col-md-6">
                                        <label htmlFor="qualificationLevel" className="form-label">
                                            Qualification Level
                                        </label>
                                        <input
                                            id="qualificationLevel"
                                            type="text"
                                            className="form-control"
                                            value={getAdjustedQualification(profile.qualification ?? "")}
                                            readOnly
                                            disabled
                                        />
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="card mt-4">
                            <div className="card-header">
                                User Information
                            </div>
                            <div className="card-body">

                                <div className="row mb-3 align-items-center">
                                    <div className="col-md-8">
                                        <label htmlFor="username" className="form-label">
                                            Username
                                        </label>
                                        <input
                                            id="username"
                                            type="text"
                                            className="form-control"
                                            value={profile.username ?? ""}
                                            readOnly
                                            disabled
                                        />
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>

                    {/* RIGHT COLUMN */}
                    <div className="col-md-4">
                        <div className="card">
                            <div className="card-header">
                                User Actions
                            </div>
                            <div className="card-body">

                                <div className="d-flex flex-column align-items-center gap-3 m-1">
                                    <button
                                        type="button"
                                        className="btn btn-primary w-75"
                                        data-bs-toggle="modal"
                                        data-bs-target="#changePWModal"
                                        >
                                        Change Password
                                    </button>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>
            </div>

            {/*The change PW modal*/}
            <div className="modal fade" id="changePWModal" tabIndex="-1" aria-hidden="true">
                <div className="modal-dialog">
                    <div className="modal-content">

                        <div className="modal-header">
                            <h5 className="modal-title">
                                Change Password
                            </h5>
                            <button
                                type="button"
                                className="btn-close"
                                data-bs-dismiss="modal"
                            />
                        </div>

                        <div className="modal-body">
                            <div className="row mb-3">
                                <div className="col-md-12">
                                    <PasswordInput
                                        id="oldPassword"
                                        label="Old password"
                                        value={oldPassword}
                                        onChange={e => setOldPassword(e.target.value)}
                                        placeholder="Enter current password"
                                        autoComplete="current-password"
                                        isInvalid={!!error}
                                        errorMessage={error}
                                    />
                                </div>
                            </div>

                            <div className="row mb-3">
                                <div className="col-md-12">
                                    <PasswordInput
                                        id="newPassword"
                                        label="New password"
                                        value={newPassword}
                                        onChange={e => setNewPassword(e.target.value)}
                                        placeholder="Enter new password"
                                        autoComplete="new-password"
                                        isInvalid={!!error}
                                        errorMessage={error}
                                    />
                                </div>
                            </div>

                            {error && (
                                <div className="alert alert-danger">
                                    {error}
                                </div>
                            )}
                            {success && (
                                <div className="alert alert-success">
                                    Password changed successfully.
                                </div>
                            )}
                        </div>

                        <div className="modal-footer">
                            <button
                                type="button"
                                className="btn btn-secondary"
                                data-bs-dismiss="modal"
                            >
                                Cancel
                            </button>

                            <button
                                className="btn btn-primary"
                                onClick={handleChangePassword}
                                disabled={loading}
                            >
                                {loading ? "Saving…" : "Save changes"}
                            </button>
                        </div>

                    </div>
                </div>

            </div>
        </>
    )
}