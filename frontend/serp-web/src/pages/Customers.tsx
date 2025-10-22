import { useEffect, useState } from 'react'
import api from '../api/client'

type Customer = { id: string; name: string; email?: string }

export default function Customers(){
  const [items, setItems] = useState<Customer[]>([])
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [editing, setEditing] = useState<string | null>(null)

  const load = async () => { setItems((await api.get('/api/customers')).data) }
  useEffect(()=>{ load() },[])

  const add = async () => {
    if(!name.trim()) return;
    await api.post('/api/customers', { name, email: email || undefined })
    setName(''); setEmail('')
    await load()
  }

  const save = async (c: Customer) => {
    await api.put(`/api/customers/${c.id}`, c)
    setEditing(null); await load()
  }

  const del = async (id: string) => {
    await api.delete(`/api/customers/${id}`)
    await load()
  }

  return (
    <div style={{padding:16}}>
      <h2>Customers</h2>
      <div style={{display:'flex', gap:8, marginBottom:12}}>
        <input placeholder="Name" value={name} onChange={e=>setName(e.target.value)} />
        <input placeholder="Email" value={email} onChange={e=>setEmail(e.target.value)} />
        <button onClick={add}>Hinzufügen</button>
      </div>
      <ul>
        {items.map(c => (
          <li key={c.id} style={{display:'flex', gap:8, alignItems:'center'}}>
            {editing===c.id ? (
              <>
                <input value={c.name} onChange={e=>setItems(items.map(x=>x.id===c.id?{...x, name:e.target.value}:x))} />
                <input value={c.email||''} onChange={e=>setItems(items.map(x=>x.id===c.id?{...x, email:e.target.value}:x))} />
                <button onClick={()=>save(c)}>Speichern</button>
                <button onClick={()=>setEditing(null)}>Abbrechen</button>
              </>
            ) : (
              <>
                <span>{c.name} {c.email && `(${c.email})`}</span>
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
