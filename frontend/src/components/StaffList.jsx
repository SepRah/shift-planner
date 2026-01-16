import { useEffect, useState } from "react";
import "../styles/SidebarLists.css";

/**
 * StaffList component displays a list of staff members and allows selection.
 */
export default function StaffList({ staff, onSelect, selected }) {
  const [localStaff, setLocalStaff] = useState([]);

  useEffect(() => setLocalStaff(staff ?? []), [staff]);

  return (
    <div className="staff-list">
      <h3>Staff Members</h3>
      <ul>
        {localStaff.map((member) => {
          const initials = (member.firstName?.[0] || "").toUpperCase() + (member.lastName?.[0] || "").toUpperCase();
          return (
            <li
              key={member.id}
              className={selected && selected.id === member.id ? "selected" : ""}
              onClick={() => onSelect(member)}
            >
              <span className="staff-avatar">{initials}</span>
              <span>
                {member.firstName && member.lastName
                  ? `${member.firstName} ${member.lastName}`
                  : "Unknown"}
                {member.role ? ` (${member.role})` : ""}
                {member.staffQualificationLevel ? (
                  <span style={{ color: '#5a8d6dff', fontSize: 'smaller', marginLeft: 6 }}>
                    [{member.staffQualificationLevel}]
                  </span>
                ) : null}
              </span>
            </li>
          );
        })}
      </ul>
    </div>
  );
}
