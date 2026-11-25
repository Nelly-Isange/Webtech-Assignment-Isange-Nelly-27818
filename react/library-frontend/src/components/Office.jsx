import axios from 'axios'
import { useState } from 'react'
import LibraryButton from './LibraryButton'

function Office() {
    const [officeName, setOfficeName] = useState("");
    const [officeNumber, setOfficeNumber] = useState("");
    const [offNumber, setOffNumber] = useState("");

    async function saveOffice() {
        try {
            const response = await axios.post('http://localhost:8081/api/offices/save', {
                name: officeName,
                officeNumber: Number(officeNumber),
            })
            if (response.status == 200) {
                alert("The office is saved successfully")
            } else {
                alert("office not saved")
            }
        } catch (error) {
            alert("office not saved")
        }
    }

    async function deleteOffice() {
        try {
            const response = await axios.delete('http://localhost:8081/api/offices/deleteByNumber/' + offNumber)
            if (response.status == 200) {
                alert("The office is deleted successfully")
            } else {
                alert("office not deleted")
            }
        } catch (error) {
            alert("office not deleted")
        }
    }

    return (
        <div>
            <h1>SAVE OFFICE</h1>
            <label>Office Name:</label><br/>
            <input type='text' onChange={e => setOfficeName(e.target.value)} /><br/>
            <label>Office Number:</label><br/>
            <input type='text' onChange={e => setOfficeNumber(e.target.value)} />
            <LibraryButton buttonLabel="SAVE" onClickButton={saveOffice} />

            <h2>DELETE OFFICE</h2><br/>
            <label>Office Number:</label><br/>
            <input type='text' onChange={e => setOffNumber(e.target.value)} /><br/>
            <LibraryButton buttonLabel="DELETE" onClickButton={deleteOffice} /><br/>
        </div>
    )
}

export default Office