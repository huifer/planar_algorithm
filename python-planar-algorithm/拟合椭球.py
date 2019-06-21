#! /usr/bin/env python
# -*- coding: utf-8 -*-
# __file__: 拟合椭球, 蛇皮思路
import numpy as np
import math
import random
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D

"""
- 以下三篇文章为设计方案的思路
https://wenku.baidu.com/view/35351608de80d4d8d15a4f75.html
https://ww2.mathworks.cn/matlabcentral/fileexchange/24693-ellipsoid-fit
http://www.cs.brandeis.edu/~cs155/Lecture_07_6.pdf

"""


def get_data_scope(data, divs=8):
    """
    获取输入坐标的范围
    :param data:
    :param divs:
    :return:
    """
    xyz = np.array([
        [min(data[:, 0]), max(data[:, 0])],
        [min(data[:, 1]), max(data[:, 1])],
        [min(data[:, 2]), max(data[:, 2])],
    ])
    scope = []

    divs_u = divs
    divs_v = divs * 2

    center = np.array([
        .5 * (xyz[0, 0] + xyz[0, 1]),
        .5 * (xyz[1, 0] + xyz[1, 1]),
        .5 * (xyz[2, 0] + xyz[2, 1])
    ])

    d_c = data - center
    # 求解坐标值
    r_s = np.sqrt(d_c[:, 0] ** 2. + d_c[:, 1] ** 2. + d_c[:, 2] ** 2.)
    d_s = np.array([
        r_s,
        np.arccos(d_c[:, 2] / r_s),
        np.arctan2(d_c[:, 1], d_c[:, 0])]
    ).T

    u = np.linspace(0, np.pi, num=divs_u)
    v = np.linspace(-np.pi, np.pi, num=divs_v)

    # for i in range(divs_u-1):

    for i in range(divs_u - 1):
        for j in range(divs_v - 1):
            point_i = []
            for k, point in enumerate(d_s):
                if (point[1] >= u[i] and point[1] < u[i + 1] and
                        point[2] >= v[j] and point[2] < v[j + 1]):
                    point_i.append(data[k])

            if len(point_i) > 0:
                scope.append(np.mean(np.array(point_i), axis=0))

    return np.array(scope)
    pass


def tuoqiu_nihe(X):
    """
    拟合过程
    :param X:
    :return:
    """
    x = X[:, 0]
    y = X[:, 1]
    z = X[:, 2]

    # A,B,C,D,E,F,G,H,I
    D = np.array([
        x * x + y * y - 2 * z * z,
        x * x + z * z - 2 * y * y,
        2 * x * y,
        2 * x * z,
        2 * y * z,
        2 * x,
        2 * y,
        2 * z,
        1 - 0 * x
    ])

    d_ = np.array(x * x + y * y + z * z).T
    u = np.linalg.solve(D.dot(D.T), D.dot(d_))

    # 坐标值 a,b,c
    a = np.array([u[0] + 1 * u[1] - 1])
    b = np.array([u[0] - 2 * u[1] - 1])
    c = np.array([u[1] - 2 * u[0] - 1])

    v = np.concatenate([a, b, c, u[2:]], axis=0).flatten()
    A = np.array([
        [v[0], v[3], v[4], v[6]],
        [v[3], v[1], v[5], v[7]],
        [v[4], v[5], v[2], v[8]],
        [v[6], v[7], v[8], v[9]]
    ])
    center = np.linalg.solve(- A[:3, :3], v[6:9])

    t_matrix = np.eye(4)
    t_matrix[3, :3] = center.T

    R = t_matrix.dot(A).dot(t_matrix.T)
    evals, evecs = np.linalg.eig(R[:3, :3] / -R[3, 3])
    evecs = evecs.T

    radii = np.sqrt(1. / np.abs(evals))
    radii *= np.sign(evals)

    return center, evecs, radii


def data_result(data):
    """
    计算函数封装
    :param data:
    :return:
    """
    X = get_data_scope(data=data, divs=10)
    center, evecs, radii = tuoqiu_nihe(X=X)
    return center, evecs, radii


def tuoqiu_info(data):
    """
    数据信息写入
    :param data:
    :return:
    """
    data2 = get_data_scope(data)
    center, evecs, radii = tuoqiu_nihe(X=data2)

    a, b, c = radii

    r = (a * b * c) ** (1. / 3.)

    D = np.array([[r / a, 0., 0.], [0., r / b, 0.], [0., 0., r / c]])
    transformation = evecs.dot(D).dot(evecs.T)

    print('')
    print('center: ', center)
    print('radii: ', radii)
    print('evecs: ', evecs)
    print('transformation:')
    print(transformation)
    np.savetxt('拟合椭球.txt', np.vstack((center.T, transformation)))
    pass


def tuoqiu_plot(center, radii, rotation, ax, plot_axes=False, cage_color='b', cage_alpha=0.2):
    """
    椭球展示
    :param center:
    :param radii:
    :param rotation:
    :param ax:
    :param plot_axes:
    :param cage_color:
    :param cage_alpha:
    :return:
    """
    u = np.linspace(0., 2. * np.pi, 100)
    v = np.linspace(0., np.pi, 100)

    x = radii[0] * np.outer(np.cos(u), np.sin(v))
    y = radii[1] * np.outer(np.sin(u), np.sin(v))
    z = radii[2] * np.outer(np.ones_like(u), np.cos(v))

    for i in range(len(x)):
        for j in range(len(x)):
            [x[i, j], y[i, j], z[i, j]] = np.dot([x[i, j], y[i, j], z[i, j]], rotation) + center

    if plot_axes:
        axes = np.array([[radii[0], 0.0, 0.0],
                         [0.0, radii[1], 0.0],
                         [0.0, 0.0, radii[2]]])
        for i in range(len(axes)):
            axes[i] = np.dot(axes[i], rotation)

        for p in axes:
            x_ = np.linspace(-p[0], p[0], 100) + center[0]
            y_ = np.linspace(-p[1], p[1], 100) + center[1]
            z_ = np.linspace(-p[2], p[2], 100) + center[2]
            ax.plot(x_, y_, z_, color=cage_color)

    ax.plot_wireframe(x, y, z, rstride=4, cstride=4, color=cage_color, alpha=cage_alpha)
    plt.show()
    pass


def show_tuoqiu(data):
    """
    椭球展示封装
    :param data:
    :return:
    """
    data2 = get_data_scope(data)
    center, evecs, radii = tuoqiu_nihe(X=data2)

    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')

    ax.set_aspect("equal")

    ax.scatter(data[:, 0], data[:, 1], data[:, 2], color='b')

    a, b, c = radii
    r = (a * b * c) ** (1. / 3.)

    tuoqiu_plot(center, radii, evecs, ax=ax, plot_axes=True, cage_color='g')
    # tuoqiu_plot(center, [r, r, r], evecs, ax=ax, plot_axes=True, cage_color='orange')

    pass


def calc_x(radii, y, z):
    a, b, c = radii
    x = math.sqrt(abs((1 - (y * y) / (b * b) - (z * z) / (c * c)) * (a * a)))
    return x


def calc_y(radii, x, z):
    a, b, c = radii
    y = math.sqrt(abs((1 - (x * x) / (a * a) - (z * z) / (c * c)) * (b * b)))
    return y


def calc_z(radii, x, y):
    a, b, c = radii
    z = math.sqrt(abs((1 - (x * x) / (a * a) - (y * y) / (b * b)) * (c * c)))
    return z


def nihetest():
    ## 初始化测试数据
    u = np.linspace(0, 2 * np.pi, 100)
    v = np.linspace(0, np.pi, 100)
    x = np.outer(np.cos(u), np.sin(v))
    y = np.outer(np.sin(u), np.sin(v))
    z = np.outer(np.ones(np.size(u)), np.cos(v))
    # data = np.array([[random.randint(0, 100), random.randint(0, 100), random.randint(10, 50)] for _ in range(1000)])
    # data = np.loadtxt('data.txt')

    lst = []
    for xx, yy, zz in zip(x, y, z):
        for i, j, k in zip(xx, yy, zz):
            lst.append([i, j, k])
    ## 一张图显示
    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')
    ax.set_aspect("equal")

    ax.plot_wireframe(x, y, z, color="y", alpha=0.2)

    ## 拟合数据
    data2 = get_data_scope(np.array(lst))
    center, evecs, radii = tuoqiu_nihe(X=data2)
    a, b, c = radii

    print("中心",center)
    print("三轴",radii)

    r = (a * b * c) ** (1. / 3.)
    tuoqiu_plot(center, radii, evecs, ax=ax, plot_axes=True, cage_color='black')
    plt.show()
    pass


def nihetest2():
    data = np.loadtxt('data.txt')
    data2 = get_data_scope(np.array(data))
    center, evecs, radii = tuoqiu_nihe(X=data2)
    print(center)
    print(radii)
    show_tuoqiu(data=data)
    pass


if __name__ == '__main__':
    # nihetest2()
    data = np.array([[random.randint(0, 100), random.randint(0, 100), random.randint(10, 50)] for _ in range(100)])
    np.savetxt('001.txt', data)

    data2 = get_data_scope(np.array(data))
    center, evecs, radii = tuoqiu_nihe(X=data2)
    show_tuoqiu(data)
    print(center)
    print(radii)