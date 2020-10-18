NATIONAL COVID MANAGEMENT SYSYTEM (NCMS)

First patient who having COVID-19 symptoms can register to the system.
Enter your NIC as the patient id and fill all other details.
Then NCMS will find the nearest hospital to the patient and assign a bed(patient, hospital_bed tables will be updated).
Doctor should admit the paitent and enter the severity level, and admitted_by details. 
If beds are not available patient will be send to queue.
Patient can be discharged by the director of the hospital. 
If a patient is dischared, he will be deleted from hospital_bed and one from queue is automatically assigned to that bed.
Once the patient in queue is assigned a bed, he will be deleted from queue table and others' queueIds will be updated.
Moh and any one can view Hospital level, district level and country level statistics.
Moh and doctors need to log into the system.
