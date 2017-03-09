

from __future__ import print_function
import tensorflow as tf
import numpy as np
import matplotlib.pyplot as plib

rng = np.random

arr = np.loadtxt("housing.data",dtype='float');
# print(arr);

yvalues = arr[:,len(arr[0]) - 1]
# create symbolic variables

X = tf.placeholder("float")

Y = tf.placeholder("float")

# create a shared variable for the weight matrix

w = tf.Variable(rng.randn(), name="weights")

b = tf.Variable(rng.randn(), name="bias")

# prediction function
y_model = tf.add(tf.multiply(X, w), b)


# Mean squared error

cost = tf.reduce_sum(tf.pow(y_model-Y, 2))/(2*100)

# construct an optimizer to minimize cost and fit line to my data

train_op = tf.train.GradientDescentOptimizer(0.5).minimize(cost)


# Launch the graph in a session
sess = tf.Session()

# Initializing the variables

init = tf.global_variables_initializer()


# you need to initialize variables
sess.run(init)


# for i in range(100):
#     for (x, y) in zip(xvalues, yvalues):
#         sess.run(train_op, feed_dict={X: x, Y: y})

costPlotData = [];
for x in range(0,len(arr[0]) - 1):
    xvalues = arr[:,x]
    training_cost = sess.run(cost, feed_dict={X: xvalues, Y: yvalues})
    costPlotData.append(training_cost)
    print("Training cost=", training_cost, "W=", sess.run(w), "b=", sess.run(b), '\n')


plib.plot(costPlotData)
plib.show()