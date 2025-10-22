import { useEffect, useState } from 'react'
import api from '../api/client'

type Project = { id: string; name: string; description?: string; startDate?: string; endDate?: string }

export default function Projects(){
  const [items, setItems] = useState<Project[]>([])
  const [name, setName] = useState('')
  const [description, setDesc] = useState('')
  const [editing, setEditing] = useState<string | null>(null)

  const load = async () => { setItems((await api.get('/api/projects')).data) }
  useEffect(()=>{ load() },[])

  const add = async () => {
    if(!name.trim()) return;
    await api.post('/api/projects', { name, description: description || undefined })
    setName(''); setDesc(''); await load()
  }

  const save = async (p: Project) => { await api.put(`/api/projects/${p.id}`, p); setEditing(null); await load() }
  const del = async (id: string) => { await api.delete(`/api/projects/${id}`); await load() }

  return (
    <div style={{padding:16}}>
      <h2>Projects</h2>
      <div style={{display:'flex', gap:8, marginBottom:12}}>
        <input placeholder="Name" value={name} onChange={e=>setName(e.target.value)} />
        <input placeholder="Beschreibung" value={description} onChange={e=>setDesc(e.target.value)} />
        <button onClick={add}>Hinzufügen</button>
      </div>
      <ul>
        {items.map(p => (
          <li key={p.id} style={{display:'flex', gap:8, alignItems:'center'}}>
            {editing===p.id ? (
              <>
                <input value={p.name} onChange={e=>setItems(items.map(x=>x.id===p.id?{...x, name:e.target.value}:x))} />
                <input value={p.description||''} onChange={e=>setItems(items.map(x=>x.id===p.id?{...x, description:e.target.value}:x))} />
                <button onClick={()=>save(p)}>Speichern</button>
                <button onClick={()=>setEditing(null)}>Abbrechen</button>
              </>
            ) : (
              <>
                <span>{p.name} {p.description && `– ${p.description}`}</span>
                <button onClick={()=>setEditing(p.id)}>Bearbeiten</button>
                <button onClick={()=>del(p.id)}>Löschen</button>
              </>
            )}
          </li>
        ))}
      </ul>
    </div>
  )
}
