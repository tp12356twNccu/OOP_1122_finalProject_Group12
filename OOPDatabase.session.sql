SELECT Renting_ID, RENTS.Utensil_ID, Utensil_Type FROM UTENSIL, RENTS
WHERE Customer_ID = 1
    AND Returned = 0
    AND UTENSIL.Utensil_ID = RENTS.Utensil_ID;





