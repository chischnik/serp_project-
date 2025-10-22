import { useEffect, useState } from 'react'
import { BrowserRouter, Routes, Route, Link, Navigate } from 'react-router-dom'
import LoginPage from './pages/LoginPage'
import Dashboard from './pages/Dashboard'
import Customers from './pages/Customers'
import Consultants from './pages/Consultants'
import Projects from './pages/Projects'
import Entries from './pages/Entries'
import api from './api/client'

function PrivateRoute({ children }: { children: JSX.Element }) {
  const token = localStorage.getItem('token');
  return token ? children : <Navigate to="/login" replace />;
}

export default function App(){
  const [me, setMe] = useState<any>(null);
  useEffect(()=>{
    const t = localStorage.getItem('token');
    if(t) api.get('/auth/me').then(r=>setMe(r.data)).catch(()=>setMe(null));
  },[]);

  return (
    <BrowserRouter>
      <nav style={{display:'flex', gap:16, padding:12, borderBottom:'1px solid #ddd'}}>
        <Link to="/">Dashboard</Link>
        <Link to="/customers">Customers</Link>
        <Link to="/consultants">Consultants</Link>
        <Link to="/projects">Projects</Link>
        <Link to="/entries">Entries</Link>
        <span style={{marginLeft:'auto', opacity:0.7}}>{me?.name ? `Angemeldet: ${me.name}` : ''}</span>
        <a href="#" onClick={()=>{localStorage.removeItem('token'); location.href='/login';}}>Logout</a>
      </nav>
      <Routes>
        <Route path="/login" element={<LoginPage/>} />
        <Route path="/" element={<PrivateRoute><Dashboard/></PrivateRoute>} />
        <Route path="/customers" element={<PrivateRoute><Customers/></PrivateRoute>} />
        <Route path="/consultants" element={<PrivateRoute><Consultants/></PrivateRoute>} />
        <Route path="/projects" element={<PrivateRoute><Projects/></PrivateRoute>} />
        <Route path="/entries" element={<PrivateRoute><Entries/></PrivateRoute>} />
      </Routes>
    </BrowserRouter>
  )
}
