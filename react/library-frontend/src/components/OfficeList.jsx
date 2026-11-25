
import React, { useState, useEffect } from 'react'
import axios from 'axios'

function OfficeList() {

    const [offices, setOffices] = useState([]);
    const [search, setSearch] = useState('');

    async function getAllOffices() {
        try {
            const response = await axios.get('http://localhost:8081/api/offices/all');

            if (response.status == 200) {
                setOffices(response.data);
            }
        } catch (error) {
            alert("Failed to get offices");
        }
    }

    useEffect(() => {
        getAllOffices();
    }, []);
    

async function deleteOffice(id) {
    if (window.confirm("Are you sure you want to delete this office?")) {
        try {
            const response = await axios.delete('http://localhost:8081/api/offices/delete/' + id);

            if (response.status == 200) {
                alert("The office is deleted successfully");
                getAllOffices();
            }
        } catch (error) {
            alert("Office not deleted");
        }
    }
}


return (
    <div>
        <h2>Available Offices</h2>

          <input type="text" placeholder="Search office..." value={search} onChange={(e) => setSearch(e.target.value)} />

    <table border="1">
        <thead>
             <tr>
               <th>Id</th>
               <th>Office Number</th>
               <th>Office Name</th>
               <th>Action</th>
             </tr>
        </thead>

    <tbody>
        {offices.filter((office) =>
          office.name.toLowerCase().includes(search.toLowerCase()) ||
          String(office.officeNumber).includes(search)
          ).map((office, index) => (
              <tr key={office.id}>
              <td>{index + 1}</td>
              <td>{office.officeNumber}</td>
              <td>{office.name}</td>
              <td>
              <button onClick={() => deleteOffice(office.id)}>Delete</button>
              <button disabled>Update</button>
         </td>
         </tr>
         ))}
         </tbody>
    </table>
    </div>
    )
}

export default OfficeList
