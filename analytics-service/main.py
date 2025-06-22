from fastapi import FastAPI # web framework for building APIs
from pydantic import BaseModel # validating and structuring the data
from typing import List # Popular ML library
from sklearn.linear_model import LinearRegression
import numpy as np # Works with arrays and numbers

# This creates a FastAPI app instance
app = FastAPI()

# Historical booking data
# Class inherits from BaseModel, which comes from Pydantic.
class BookingHistory(BaseModel):
    actualSeats: int
    predictedSeats: int
    bookedSeats: int
    filledSeats: int

# @PostMapping("/predict-seats")
@app.post("/predict-seats")
def predict_seats(history: List[BookingHistory]):
    if len(history) < 2:
        return {"error": "Need at least 2 data points to train model"}

    # Use all data for training
    # Prepares the input (features) from each record.
    x = np.array([[h.actualSeats, h.bookedSeats, h.filledSeats] for h in history])
    print("X = ", x)
    # Prepares the target (label)
    y = np.array([h.predictedSeats for h in history])
    print("Y = ", x)

    # Train the model
    model = LinearRegression()
    model.fit(x, y)

    # Predict for a "future" case using average of past inputs
    avg_actual = np.mean([h.actualSeats for h in history])
    print("Actual average seats ", avg_actual)
    avg_booked = np.mean([h.bookedSeats for h in history])
    print("Booked average seats ", avg_booked)
    avg_filled = np.mean([h.filledSeats for h in history])
    print("Filled average seats ", avg_filled)

    input_data = np.array([[avg_actual, avg_booked, avg_filled]])
    print("Input data ", input_data)
    predicted_data = model.predict(input_data)
    print("Prediction arr value ", predicted_data)
    predicted = predicted_data[0]
    print("Predicted seat count ", predicted)

    return {
        "predictedSeats": int(predicted),
        "note": "Predicted based on historical average inputs"
    }
