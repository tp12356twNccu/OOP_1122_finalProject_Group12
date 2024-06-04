SELECT COUNT(Renting_ID) 
FROM RENTS JOIN UTENSIL ON UTENSIL.Utensil_ID = RENTS.Utensil_ID 
WHERE Utensil_Type = 'C' 
    AND Returned = 0
    AND Customer_ID = 1;



