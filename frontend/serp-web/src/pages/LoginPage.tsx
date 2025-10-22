import { useState } from 'react'
import api from '../api/client'

export default function LoginPage(){
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')

  const submit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError('')
    try {
      const res = await api.post('/auth/login', { username, password })
      localStorage.setItem('token', res.data.token)
      window.location.href = '/'
    } catch {
      setError('Login fehlgeschlagen')
    }
  }

  return (
    <form onSubmit={submit} style={{maxWidth:360,margin:'48px auto',display:'grid',gap:12}}>
      <h2>Login</h2>
      <input placeholder="Username" value={username} onChange={e=>setUsername(e.target.value)} />
      <input placeholder="Password" type="password" value={password} onChange={e=>setPassword(e.target.value)} />
      {error && <div style={{color:'red'}}>{error}</div>}
      <button>Einloggen</button>
    </form>
  )
}
