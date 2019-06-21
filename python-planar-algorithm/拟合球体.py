#! /usr/bin/env python
# -*- coding: utf-8 -*-
# __file__: 拟合球体
# __date__:
# __author__: huifer
import numpy as np
import math
from matplotlib import pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
import csv


def qiuti(x_list, y_list, z_list):
    """
    球体计算
    :param x_list:
    :param y_list:
    :param z_list:
    :return:
    """
    # 右侧矩阵
    x_list = np.array(x_list)
    y_list = np.array(y_list)
    z_list = np.array(z_list)

    right_mat = np.zeros((len(x_list), 4))
    right_mat[:, 0] = x_list * 2
    right_mat[:, 1] = y_list * 2
    right_mat[:, 2] = z_list * 2
    right_mat[:, 3] = 1

    # 等号左侧
    left_mat = np.zeros((len(x_list), 1))
    left_mat[:, 0] = (x_list * x_list) + (y_list * y_list) + (z_list * z_list)

    C, residules, rank, singval = np.linalg.lstsq(right_mat, left_mat)

    # 求半径
    t = (C[0] * C[0]) + (C[1] * C[1]) + (C[2] * C[2]) + C[3]
    radius = math.sqrt(t)
    return radius, C[0], C[1], C[2]


def show_qiut(x_list, y_list, z_list):
    """
    三维展示球体
    :param x_list:
    :param y_list:
    :param z_list:
    :return:
    """

    fig = plt.figure()
    ax = plt.axes(projection='3d')

    # 计算拟合
    r, x0, y0, z0 = qiuti(x_list, y_list, z_list)
    print("圆心:", [x0[0], y0[0], z0[0]])
    print("半径:", r)

    u, v = np.mgrid[0:2 * np.pi:20j, 0:np.pi:10j]

    x = np.cos(u) * np.sin(v) * r
    y = np.sin(u) * np.sin(v) * r
    z = np.cos(v) * r

    x = x + x0
    y = y + y0
    z = z + z0

    # 三维展示

    ax.scatter(x_list, y_list, z_list, zdir='z', s=20, c='b', rasterized=True)
    ax.plot_wireframe(x, y, z, color="r")

    ax.set_aspect('equal')

    plt.show()


def write_csv(x, y, z):
    r, x0, y0, z0 = qiuti(x, y, z)

    with open('qiuti.csv', "w") as csv_file:
        csv_file.write("x,y,z,r\n")
        for x_, y_, z_ in zip(x, y, z):
            csv_file.write("{},{},{},{}\n".format(x_, y_, z_, 0.5))

        csv_file.write("{},{},{},{}\n".format(x0[0], y0[0], z0[0], r))
    pass


if __name__ == '__main__':
    random_x = np.random.randint(0, 10, 30)
    random_y = np.random.randint(0, 10, 30)
    random_z = np.random.randint(0, 10, 30)

    show_qiut(x_list=random_x, y_list=random_y, z_list=random_z)
    write_csv(random_x, random_y, random_z)
