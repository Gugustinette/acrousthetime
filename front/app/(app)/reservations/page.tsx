"use client"
import Input from '@/app/components/inputs/Input'
import styles from './Reservations.module.scss'
import Select from '@/app/components/inputs/select/Select'
import Button from '@/app/components/button/Button'
import Title from '@/app/components/title/Title'
import Chips from '@/app/components/chips/Chips'
import ReservationComponent from './reservation/Reservation'
import { Reservation, Salle, StatusReservation } from '@/app/api/types'
import { useEffect, useState } from 'react'
import { deleteReservation, getReservationsByUser, postReservation } from '@/app/api/requests/requests_reservation'
import { getSallesDispo } from '@/app/api/requests/requests_salles'

export default function Reservations() {

  let [reservations, setReservations] = useState([] as Reservation[])
  let [salles, setSalles] = useState([] as Salle[])

  useEffect(() => {
    getReservationsByUser().then((res) => {
      setReservations(res);
    });
  }, []);

  const [filterByAccepted, setFilterByAccepted] = useState(false);
  const [filterByPending, setFilterByPending] = useState(false);
  const [filterByRefused, setFilterByRefused] = useState(false);
  const filterOn = filterByAccepted || filterByPending || filterByRefused;

  const reservationsByMonthAndYear = groupReservationsByMonthAndYear(reservations);

  function groupReservationsByMonthAndYear(reservations: Reservation[]) {
    return reservations.reduce((groups: Record<string, Reservation[]>, reservation) => {
      const reservationDate = new Date(reservation.dt_start);
      const month = reservationDate.toLocaleString('default', { month: 'long' });
      const capitalizedMonth = month.charAt(0).toUpperCase() + month.slice(1);
      const year = reservationDate.getFullYear();

      const groupKey = `${capitalizedMonth} ${year}`;
      if (!groups[groupKey]) {
        groups[groupKey] = [];
      }

      groups[groupKey].push(reservation);

      return groups;
    }, {});
  }

  const filteredReservations = Object.entries(reservationsByMonthAndYear)
    .sort((a, b) => {
      const dateA = new Date(a[0]);
      const dateB = new Date(b[0]);
      return dateB.getTime() - dateA.getTime();
    })
    .reduce((filteredGroups: Record<string, Reservation[]>, [monthAndYear, reservations]) => {
      const filteredReservations = filterOn
        ? reservations.filter((reservation: Reservation) => {
          if (filterByAccepted && reservation.status === StatusReservation.ACCEPTED) {
            return true;
          }
          if (filterByPending && reservation.status === StatusReservation.PENDING) {
            return true;
          }
          if (filterByRefused && reservation.status === StatusReservation.REFUSED) {
            return true;
          }
          return false;
        })
        : reservations;

      if (filteredReservations.length > 0) {
        filteredGroups[monthAndYear] = filteredReservations;
      }

      return filteredGroups;
    }, {});

  function submitForm(event: any) {
    event.preventDefault();

    const salle = salles.find((salle) => salle.id === event.target.salle.value.split('@@@')[0]);

    if (salle) {
      const reservation = {
        dt_start: new Date(event.target.date.value + 'T' + event.target.heureDebut.value),
        dt_end: new Date(event.target.date.value + 'T' + event.target.heureFin.value),
        status: StatusReservation.PENDING,
        salle: salle,
        description: ''
      }

      postReservation(reservation).then((res) => {
        setReservations([...reservations, res]);
        console.log(res);
      }).catch((err) => {
        console.log(err);
      })
    }
  }

  function handleChange(event: React.ChangeEvent<HTMLInputElement>) {
    const { name, value } = event.target;

    if (name === 'date' || name === 'heureDebut' || name === 'heureFin') {
      const startDate = new Date();
      const endDate = new Date();

      if (name === 'date') {
        startDate.setFullYear(Number(value.split('-')[0]), Number(value.split('-')[1]) - 1, Number(value.split('-')[2]));
        endDate.setFullYear(Number(value.split('-')[0]), Number(value.split('-')[1]) - 1, Number(value.split('-')[2]));
      } else if (name === 'heureDebut') {
        startDate.setHours(Number(value.split(':')[0]), Number(value.split(':')[1]), 0, 0);
      } else if (name === 'heureFin') {
        endDate.setHours(Number(value.split(':')[0]), Number(value.split(':')[1]), 0, 0);
      }

      getSallesDispo(startDate, endDate).then((res) => {
        setSalles(res);
        console.log(res);
      }).catch((err) => {
        console.log(err);
      })
    }
  }

  function deleteReserv(reservationId: string) {
    deleteReservation(reservationId).then(() => {
      setReservations((prevReservations) =>
        prevReservations.filter((reservation) => reservation.id !== reservationId)
      );
    }).catch((error) => {
      console.log(error);
    });
  }

  return (
    <div className={`${styles.main}`}>
      <div>
        <Title title='Demander une réservation' />
        <form className={`${styles.form}`} onSubmit={submitForm}>
          <span>Réserver le </span>
          <Input name='date' type='date' color='blue' onChange={handleChange} />
          <span>de</span>
          <Input name='heureDebut' type='time' color='blue' onChange={handleChange} />
          <span>à</span>
          <Input name='heureFin' type='time' color='blue' onChange={handleChange} />
          <span>la salle </span>
          <Select name='salle' options={salles} optionLabel='name' optionValue='id' color='blue' />
          <Button type='submit' label='Demander' color='darkBlue' />
        </form>
      </div>

      <div>
        <Title title='Mes réservations' />
        <div className={`${styles.hgroup} ${styles.chips}`}>
          <Chips label="Acceptées" color="green" active={filterByAccepted} setFilter={(value: boolean) => setFilterByAccepted(value)} />
          <Chips label="En attente" color="orange" active={filterByPending} setFilter={(value: boolean) => setFilterByPending(value)} />
          <Chips label="Refusées" color="red" active={filterByRefused} setFilter={(value: boolean) => setFilterByRefused(value)} />
        </div>

        {Object.entries(filteredReservations).map(([monthAndYear, reservations]) => (
          <div key={monthAndYear} className={`${styles.month}`}>
            <h2>{monthAndYear}</h2>
            {reservations.map((reservation: Reservation, i: number) => (
              <div key={i} className={`${styles.reservation}`}>
                <ReservationComponent reservation={reservation} onDelete={deleteReserv} />
              </div>
            ))}
          </div>
        ))}
      </div>
    </div>
  )
}
