import { useEffect, useState } from 'react'
import api from '../api/client'

type Entry = { id: string; projectId?: string; consultantId?: string; workDate?: string; hours?: number; notes?: string }
type Project = { id: string; name: string }
type Consultant = { id: string; firstName: string; lastName: string }

export default function Entries(){
  const [items, setItems] = useState<Entry[]>([])
  const [projects, setProjects] = useState<Project[]>([])
  const [consultants, setConsultants] = useState<Consultant[]>([])

  const [entry, setEntry] = useState<Entry>({ id: '', hours: 1 })

  const load = async () => {
    const [es, ps, cs] = await Promise.all([
      api.get('/api/entries'),
      api.get('/api/projects'),
      api.get('/api/consultants')
    ])
    setItems(es.data); setProjects(ps.data); setConsultants(cs.data)
  }
  useEffect(()=>{ load() },[])

  const add = async () => {
    await api.post('/api/entries', {
      projectId: entry.projectId || undefined,
      consultantId: entry.consultantId || undefined,
      workDate: entry.workDate || undefined,
      hours: entry.hours || 1,
      notes: entry.notes || undefined
    })
    setEntry({ id:'', hours: 1 }); await load()
  }

  const del = async (id: string) => { await api.delete(`/api/entries/${id}`); await load() }

  return (
    <div style={{padding:16}}>
      <h2>Entries</h2>
      <div style={{display:'grid', gridTemplateColumns:'1fr 1fr 1fr 1fr 2fr auto', gap:8, alignItems:'center', marginBottom:12}}>
        <select value={entry.projectId||''} onChange={e=>setEntry({...entry, projectId: e.target.value||undefined})}>
          <option value=''>Projekt wählen</option>
          {projects.map(p=>(<option key={p.id} value={p.id}>{p.name}</option>))}
        </select>
        <select value={entry.consultantId||''} onChange={e=>setEntry({...entry, consultantId: e.target.value||undefined})}>
          <option value=''>Consultant wählen</option>
          {consultants.map(c=>(<option key={c.id} value={c.id}>{c.firstName} {c.lastName}</option>))}
        </select>
        <input type="date" value={entry.workDate||''} onChange={e=>setEntry({...entry, workDate: e.target.value})} />
        <input type="number" step="0.25" value={entry.hours||1} onChange={e=>setEntry({...entry, hours: Number(e.target.value)})} />
        <input placeholder="Notizen" value={entry.notes||''} onChange={e=>setEntry({...entry, notes: e.target.value})} />
        <button onClick={add}>Hinzufügen</button>
      </div>
      <ul>
        {items.map(e => (
          <li key={e.id} style={{display:'flex', gap:12}}>
            <span>{e.workDate || '—'}</span>
            <span>{e.hours ?? '1'}h</span>
            <span>Proj: {e.projectId?.slice(0,8) || '—'}</span>
            <span>Cons: {e.consultantId?.slice(0,8) || '—'}</span>
            <span>{e.notes || ''}</span>
            <button onClick={()=>del(e.id)}>Löschen</button>
          </li>
        ))}
      </ul>
    </div>
  )
}
