import { useState } from 'react'
import heroImg from './assets/hero.png'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import './App.css'
import LibraryButton from './components/LibraryButton'
import LibraryBooks from './components/LibraryBooks'
import LibraryFooter from './components/LibraryFooter'
import LibraryHeader from './components/LibraryHeader'
import LibraryLogin from './components/LibraryLogin'
import Office from './components/Office'
import OfficeList from './components/OfficeList'

function App() {
  

  return (
    <div>
      <LibraryHeader />
      <main>
        
        
        <Office/>
        <OfficeList />
      </main>
     
      
    </div>
  );
}

export default App
