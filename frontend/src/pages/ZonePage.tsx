import { useEffect,useState } from "react";
import { useNavigate } from "react-router-dom";


type Zone={
 id:number;
 name:string;
};


export default function ZonePage(){

 const [zones,setZones]=useState<Zone[]>([]);
 const navigate=useNavigate();


 useEffect(()=>{

  fetch(
    "http://localhost:8080/api/zones"
  )
  .then(r=>r.json())
  .then(setZones)
  .catch(console.error);

 },[]);



 const removeZone = async(id:number)=>{

  await fetch(
    `http://localhost:8080/api/zones/${id}`,
    {
      method:"DELETE"
    }
  );


  setZones(
    zones.filter(z=>z.id!==id)
  );

 };



 return (

 <div
 style={{
  padding:20,
  background:"#f0f2f5",
  minHeight:"100vh",
 }}
 >

 <button
 onClick={()=>navigate("/zones/create")}
 style={{
  padding:12,
  background:"#d21951",
  color:"#fff",
  border:"none",
  borderRadius:8,
 }}
 >
 Создать зону
 </button>



 {
 zones.map(zone=>(

 <div
 key={zone.id}
 style={{
  marginTop:15,
  background:"#fff",
  padding:15,
  borderRadius:8,
 }}
 >

 {zone.name}


 <button
 onClick={()=>
 navigate(`/zones/${zone.id}`)
 }
 style={{
  marginLeft:20,
 }}
 >
 Открыть
 </button>


 <button
 onClick={()=>
 removeZone(zone.id)
 }
 style={{
  marginLeft:10,
 }}
 >
 Удалить
 </button>


 </div>

 ))
 }


 </div>

 );

}