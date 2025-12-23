import React, { useEffect, useState } from "react";

export default function StaffList({ staff, onSelect, selected }) {
  const [localStaff, setLocalStaff] = useState([]);

  useEffect(() => {
    if (staff && staff.length > 0) {
      setLocalStaff(staff);
    } else {
      // Dummy-Liste, falls keine StaffMember geladen werden konnten
      setLocalStaff([
        { id: "dummyA", name: "Mitarbeiter A" },
        { id: "dummyB", name: "Mitarbeiter B" },
        { id: "dummyC", name: "Mitarbeiter C" },
      ]);
    }
  }, [staff]);

  return (
    <div>
      <h3>Staff Members</h3>
      <ul style={{ listStyle: "none", padding: 0 }}>
        {localStaff.map((member) => (
          <li
            key={member.id}
            style={{
              padding: "8px",
              margin: "4px 0",
              borderRadius: "4px",
              background:
                selected && selected.id === member.id ? "#ffe58f" : "#fff",
              border: "1px solid #ddd",
              cursor: "pointer",
            }}
            onClick={() => onSelect(member)}
          >
            {member.firstName && member.lastName
                ? `${member.firstName} ${member.lastName}`
                : "Unknown"}
            {member.role ? ` (${member.role})` : ""}
          </li>
        ))}
      </ul>
    </div>
  );
}
