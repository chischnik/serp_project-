import { useEffect, useState } from 'react'
import api from '../api/client'

type Consultant = { id: string; firstName: string; lastName: string; email?: string }

export default function Consultants(){
  const [items, setItems] = useState<Consultant[]>([])
  const [firstName, setFirst] = useState('')
  const [lastName, setLast] = useState('')
  const [email, setEmail] = useState('')
  const [editing, setEditing] = useState<string | null>(null)

  const load = async () => { setItems((await api.get('/api/consultants')).data) }
  useEffect(()=>{ load() },[])

  const add = async () => {
    if(!firstName.trim() || !lastName.trim()) return;
    await api.post('/api/consultants', { firstName, lastName, email: email || undefined })
    setFirst(''); setLast(''); setEmail(''); await load()
  }

  const save = async (c: Consultant) => {
    await api.put(`/api/consultants/${c.id}`, c); setEditing(null); await load()
  }

  const del = async (id: string) => { await api.delete(`/api/consultants/${id}`); await load() }

  return (
    <div style={{padding:16}}>
      <h2>Consultants</h2>
      <div style={{display:'flex', gap:8, marginBottom:12}}>
        <input placeholder="Vorname" value={firstName} onChange={e=>setFirst(e.target.value)} />
        <input placeholder="Nachname" value={lastName} onChange={e=>setLast(e.target.value)} />
        <input placeholder="Email" value={email} onChange={e=>setEmail(e.target.value)} />
        <button onClick={add}>Hinzufügen</button>
      </div>
      <ul>
        {items.map(c => (
          <li key={c.id} style={{display:'flex', gap:8, alignItems:'center'}}>
            {editing===c.id ? (
              <>
                <input value={c.firstName} onChange={e=>setItems(items.map(x=>x.id===c.id?{...x, firstName:e.target.value}:x))} />
                <input value={c.lastName} onChange={e=>setItems(items.map(x=>x.id===c.id?{...x, lastName:e.target.value}:x))} />
                <input value={c.email||''} onChange={e=>setItems(items.map(x=>x.id===c.id?{...x, email:e.target.value}:x))} />
                <button onClick={()=>save(c)}>Speichern</button>
                <button onClick={()=>setEditing(null)}>Abbrechen</button>
              </>
            ) : (
              <>
                <span>{c.firstName} {c.lastName} {c.email && `(${c.email})`}</span>
                <button onClick={()=>setEditing(c.id)}>Bearbeiten</button>
                <button onClick={()=>del(c.id)}>Löschen</button>
              </>
            )}
          </li>
        ))}
      </ul>
    </div>
  )
}
