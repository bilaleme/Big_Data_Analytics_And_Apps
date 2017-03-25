import time
import numpy as np
import os
from random import shuffle
from PIL import Image

import tensorflow as tf

# print(len(mnist.train.next_batch(1)[0][0]))
# print(mnist.train.next_batch(2))

start = int(round(time.time() * 1000))
classes = 4

# Load from and save to
Names = [['./Train', 'train'], ['./Test', 'test']]
trainPath = Names[0][0]
testPath = Names[1][0]

#training array
data_train = []
#testing array
data_test = []

#training data preparation
FileList = []

for dirname in os.listdir(trainPath)[1:]:
    path = os.path.join(trainPath, dirname)
    for filename in os.listdir(path):
        if filename.endswith(".png"):
            FileList.append(os.path.join(trainPath, dirname, filename))

shuffle(FileList)  # Usefull for further segmenting the validation set

for filename in FileList:

    data_image = []
    data_label = []

    label = int(filename.split('/')[2])

    Im = Image.open(filename)

    pixel = Im.load()

    width, height = Im.size

    for x in range(0, width):
        for y in range(0, height):
            data_image.append(pixel[y, x]/255)

    zeroArray = [0]*classes
    zeroArray[label - 1] = 1
    # print(zeroArray)
    data_label.append(zeroArray)
    data_train.append([data_image,zeroArray])


# test data preparation

FileList = []

for dirname in os.listdir(testPath)[1:]:
    path = os.path.join(testPath, dirname)
    for filename in os.listdir(path):
        if filename.endswith(".png"):
            FileList.append(os.path.join(testPath, dirname, filename))

shuffle(FileList)

for filename in FileList:

    data_image = []
    data_label = []

    label = int(filename.split('/')[2])

    Im = Image.open(filename)

    pixel = Im.load()

    width, height = Im.size

    for x in range(0, width):
        for y in range(0, height):
            data_image.append(pixel[y, x]/255)

    zeroArray = [0]*classes
    zeroArray[label - 1] = 1
    # print(zeroArray)
    # data_label.append(zeroArray)
    data_test.append([data_image,zeroArray])


data_train= np.array(data_train)
data_test = np.array(data_test)

testX = []
for x in data_test :
    testX.append(x[0])

testY = []
for y in data_test :
    testY.append(y[1])

sess = tf.InteractiveSession()
x = tf.placeholder(tf.float32, shape=[None, 784])
y_ = tf.placeholder(tf.float32, shape=[None, 4])
sess.run(tf.global_variables_initializer())


def weight_variable(shape):
    initial = tf.truncated_normal(shape, stddev=0.1)
    return tf.Variable(initial)


def bias_variable(shape):
    initial = tf.constant(0.1, shape=shape)
    return tf.Variable(initial)


def conv2d(x, W):
    return tf.nn.conv2d(x, W, strides=[1, 1, 1, 1], padding='SAME')


def max_pool_2x2(x):
    return tf.nn.max_pool(x, ksize=[1, 2, 2, 1], strides=[1, 2, 2, 1], padding='SAME')


W_conv1 = weight_variable([5, 5, 1, 32])
b_conv1 = bias_variable([32])
x_image = tf.reshape(x, [-1, 28, 28, 1])
h_conv1 = tf.nn.relu(conv2d(x_image, W_conv1) + b_conv1)
h_pool1 = max_pool_2x2(h_conv1)


W_conv2 = weight_variable([5, 5, 32, 64])
b_conv2 = bias_variable([64])

h_conv2 = tf.nn.relu(conv2d(h_pool1, W_conv2) + b_conv2)
h_pool2 = max_pool_2x2(h_conv2)

W_fc1 = weight_variable([7 * 7 * 64, 1024])
b_fc1 = bias_variable([1024])

h_pool2_flat = tf.reshape(h_pool2, [-1, 7 * 7 * 64])
h_fc1 = tf.nn.relu(tf.matmul(h_pool2_flat, W_fc1) + b_fc1)
# Dropout
keep_prob = tf.placeholder(tf.float32)
h_fc1_drop = tf.nn.dropout(h_fc1, keep_prob)
# readout layer
W_fc2 = weight_variable([1024, 4])
b_fc2 = bias_variable([4])

y_conv = tf.matmul(h_fc1_drop, W_fc2) + b_fc2
cross_entropy = tf.reduce_mean(tf.nn.softmax_cross_entropy_with_logits(logits=y_conv, labels=y_))

train_step = tf.train.AdamOptimizer(1e-4).minimize(cross_entropy)
correct_prediction = tf.equal(tf.argmax(y_conv, 1), tf.argmax(y_, 1))
accuracy = tf.reduce_mean(tf.cast(correct_prediction, tf.float32))

tf.summary.scalar('cross_entropy', cross_entropy)

tf.summary.histogram('cross_hist', cross_entropy)
merged = tf.summary.merge_all()
accuracySummary = tf.summary.scalar('Accuracy', accuracy)
accuracyHistogram = tf.summary.histogram('Accuracy-Histogram', accuracy)

accuMerge = tf.summary.merge_all()

trainwriter = tf.summary.FileWriter('data/logs', sess.graph)
sess.run(tf.global_variables_initializer())
for i in range(0,63,7):

    trainX = []
    trainY = []
    for r in range(i,i+7):
        trainX.append(data_train[r,0])
        trainY.append(data_train[r,1])

    summary, _ = sess.run([merged, train_step], feed_dict={x: trainX, y_: trainY, keep_prob: 0.5})
    trainwriter.add_summary(summary, i)

    summary1, _ = sess.run([accuMerge, train_step], feed_dict={x: trainX, y_: trainY, keep_prob: 0.5})
    trainwriter.add_summary(summary1, i)

    if i % 7 == 0:
        train_accuracy = accuracy.eval(feed_dict={x: trainX, y_: trainY, keep_prob: 1.0})
        print("step %d, training accuracy %g" % (i, train_accuracy))

print("test accuracy %g" % accuracy.eval(feed_dict={x: testX, y_: testY, keep_prob: 1.0}))
end = int(round(time.time() * 1000))
print("Time for building convnet: ")
print(end - start)