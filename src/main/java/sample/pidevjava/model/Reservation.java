package sample.pidevjava.model;
import java.sql.Time;
import java.util.Date;
public class Reservation {

        public int id_reservation;
        private Date date_reserve;
        private Time temps_reservation;
        private TypeTerrain type;
        private float prix_reservation;
        public int id;
        public int id_terrain;
        private Time duree_reservation;


        public Reservation(){

        }
                // nihit id reservation mil constructor

        public Reservation( Date date_reserve, Time temps_reservation, TypeTerrain type, float prix_reservation, int id_user,int id_terrain,Time duree_reservation) {
           // this.id_reservation = id_reservation;
            this.date_reserve = date_reserve;
            this.temps_reservation = temps_reservation;
            this.type = type;
            this.prix_reservation = prix_reservation;
            this.id=id_user;
            this.id_terrain=id_terrain;
            this.duree_reservation=duree_reservation;
        }

        public int getId_reservation() {
            return id_reservation;
        }

    public void setId_reservation(int id_reservation) {
        this.id_reservation = id_reservation;
    }

        public java.sql.Date getDate_reserve() {
            return (java.sql.Date) date_reserve;
        }

        public void setDate_reserve(Date date_reservé) {
            this.date_reserve = date_reservé;
        }

        public Time getTemps_reservation() {
            return temps_reservation;
        }

        public void setTemps_reservation(Time temps_reservé) {
            this.temps_reservation = temps_reservé;
        }

    public TypeTerrain getType() {
        return type;
    }

    public void setType(TypeTerrain type) {
        this.type = type;
    }

    public float getPrix_reservation() {
            return prix_reservation;
        }

        public void setPrix_reservation(float prix_reservation) {
            this.prix_reservation = prix_reservation;
        }


        public int getId() {
            return id;
        }

        public void setId(int id_user) {
            this.id = id_user;
        }

    public int getId_terrain() {
        return id_terrain;
    }

    public void setId_terrain(int id_terrain) {
        this.id_terrain = id_terrain;
    }

    public Time getDuree_reservation() {
        return duree_reservation;
    }

    public void setDuree_reservation(Time duree_reservation) {
        this.duree_reservation = duree_reservation;
    }

    @Override
        public String toString() {
            return "Reservation{" +
                    "id_reservation=" + id_reservation +
                    ", date_reserve='" + date_reserve + '\'' +
                    ", temps_reservation='" + temps_reservation + '\'' +
                    ", type='" + type + '\'' +
                    ", prix_reservation=" + prix_reservation +
                    ", id_user=" + id +
                    '}';
        }
    }


