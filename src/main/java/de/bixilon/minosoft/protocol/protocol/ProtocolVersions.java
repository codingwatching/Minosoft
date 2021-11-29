/*
 * Minosoft
 * Copyright (C) 2020 Moritz Zwerger
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
 *
 * This software is not affiliated with Mojang AB, the original developer of Minecraft.
 */

package de.bixilon.minosoft.protocol.protocol;

@SuppressWarnings("unused")
public class ProtocolVersions {
    public static final int
            V_1_18_PRE5 = 807,
            V_1_18_PRE4 = 806,
            V_1_18_PRE3 = 805,
            V_1_18_PRE2 = 804,
            V_1_18_PRE1 = 803,
            V_21W44A = 802,
            V_21W43A = 801,
            V_21W42A = 800,
            V_21W41A = 799,
            V_21W40A = 798,
            V_21W39A = 797,
            V_21W38A = 796,
            V_21W37A = 795,
            V_1_17_1 = 794,
            V_1_17_1_RC2 = 793,
            V_1_17_1_RC1 = 792,
            V_1_17_1_PRE_3 = 791,
            V_1_17_1_PRE_2 = 790,
            V_1_17_1_PRE_1 = 789,
            V_1_17 = 788,
            V_1_17_RC2 = 787,
            V_1_17_RC1 = 786,
            V_1_17_PRE5 = 785,
            V_1_17_PRE4 = 784,
            V_1_17_PRE3 = 783,
            V_1_17_PRE2 = 782,
            V_1_17_PRE1 = 781,
            V_21W20A = 780,
            V_21W19A = 779,
            V_21W18A = 778,
            V_21W17A = 777,
            V_21W16A = 776,
            V_21W15A = 775,
            V_21W14A = 774,
            V_21W13A = 773,
            V_21W11A = 772,
            V_21W10A = 771,
            V_21W08B = 770,
            V_21W08A = 769,
            V_21W07A = 768,
            V_21W06A = 767,
            V_21W05B = 766,
            V_21W05A = 765,
            V_21W03A = 764,
            V_20W51A = 763,
            V_20W49A = 762,
            V_20W48A = 761,
            V_20W46A = 760,
            V_20W45A = 759,
            V_1_16_5 = 758, // dirty workaround, they got released after 20w51a
            V_1_16_5_RC1 = 757,
            V_1_16_4_RC1 = 756,
            V_1_16_4_PRE2 = 755,
            V_1_16_4_PRE1 = 754,
            V_1_16_3 = 753,
            V_1_16_3_RC1 = 752,
            V_1_16_2 = 751,
            V_1_16_2_RC2 = 750,
            V_1_16_2_RC1 = 749,
            V_1_16_2_PRE3 = 748,
            V_1_16_2_PRE2 = 746,
            V_1_16_2_PRE1 = 744,
            V_20W30A = 743,
            V_20W29A = 741,
            V_20W28A = 740,
            V_20W27A = 738,
            V_1_16_1 = 736,
            V_1_16 = 735,
            V_1_16_RC1 = 734,
            V_1_16_PRE8 = 733,
            V_1_16_PRE7 = 732,
            V_1_16_PRE6 = 730,
            V_1_16_PRE5 = 729,
            V_1_16_PRE4 = 727,
            V_1_16_PRE3 = 725,
            V_1_16_PRE2 = 722,
            V_1_16_PRE1 = 721,
            V_20W22A = 719,
            V_20W21A = 718,
            V_20W20B = 717,
            V_20W20A = 716,
            V_20W19A = 715,
            V_20W18A = 714,
            V_20W17A = 713,
            V_20W16A = 712,
            V_20W15A = 711,
            V_20W14A = 710,
            V_20W13B = 709,
            V_20W13A = 708,
            V_20W12A = 707,
            V_20W11A = 706,
            V_20W10A = 705,
            V_20W09A = 704,
            V_20W08A = 703,
            V_20W07A = 702,
            V_20W06A = 701,
            V_1_15_2 = 578,
            V_1_15_2_PRE2 = 577,
            V_1_15_2_PRE1 = 576,
            V_1_15_1 = 575,
            V_1_15_1_PRE1 = 574,
            V_1_15 = 573,
            V_1_15_PRE7 = 572,
            V_1_15_PRE6 = 571,
            V_1_15_PRE5 = 570,
            V_1_15_PRE4 = 569,
            V_1_15_PRE3 = 567,
            V_1_15_PRE2 = 566,
            V_1_15_PRE1 = 565,
            V_19W46B = 564,
            V_19W46A = 563,
            V_19W45B = 562,
            V_19W45A = 561,
            V_19W44A = 560,
            V_19W42A = 559,
            V_19W41A = 558,
            V_19W40A = 557,
            V_19W39A = 556,
            V_19W38B = 555,
            V_19W38A = 554,
            V_19W37A = 553,
            V_19W36A = 552,
            V_19W35A = 551,
            V_19W34A = 550,
            V_1_14_4 = 498,
            V_1_14_4_PRE7 = 497,
            V_1_14_4_PRE6 = 496,
            V_1_14_4_PRE5 = 495,
            V_1_14_4_PRE4 = 494,
            V_1_14_4_PRE3 = 493,
            V_1_14_4_PRE2 = 492,
            V_1_14_4_PRE1 = 491,
            V_1_14_3 = 490,
            V_1_14_3_PRE4 = 489,
            V_1_14_3_PRE3 = 488,
            V_1_14_3_PRE2 = 487,
            V_1_14_3_PRE1 = 486,
            V_1_14_2 = 485,
            V_1_14_2_PRE4 = 484,
            V_1_14_2_PRE3 = 483,
            V_1_14_2_PRE2 = 482,
            V_1_14_2_PRE1 = 481,
            V_1_14_1 = 480,
            V_1_14_1_PRE2 = 479,
            V_1_14_1_PRE1 = 478,
            V_1_14 = 477,
            V_1_14_PRE5 = 476,
            V_1_14_PRE4 = 475,
            V_1_14_PRE3 = 474,
            V_1_14_PRE2 = 473,
            V_1_14_PRE1 = 472,
            V_19W14B = 471,
            V_19W14A = 470,
            V_19W13B = 469,
            V_19W13A = 468,
            V_19W12B = 467,
            V_19W12A = 466,
            V_19W11B = 465,
            V_19W11A = 464,
            V_19W09A = 463,
            V_19W08B = 462,
            V_19W08A = 461,
            V_19W07A = 460,
            V_19W06A = 459,
            V_19W05A = 458,
            V_19W04B = 457,
            V_19W04A = 456,
            V_19W03C = 455,
            V_19W03B = 454,
            V_19W03A = 453,
            V_19W02A = 452,
            V_18W50A = 451,
            V_18W49A = 450,
            V_18W48B = 449,
            V_18W48A = 448,
            V_18W47B = 447,
            V_18W47A = 446,
            V_18W46A = 445,
            V_18W45A = 444,
            V_18W44A = 443,
            V_18W43C = 442,
            V_18W43B = 441,
            V_18W43A = 440,
            V_1_13_2 = 404,
            V_1_13_2_PRE2 = 403,
            V_1_13_2_PRE1 = 402,
            V_1_13_1 = 401,
            V_1_13_1_PRE2 = 400,
            V_1_13_1_PRE1 = 399,
            V_18W33A = 398,
            V_18W32A = 397,
            V_18W31A = 396,
            V_18W30B = 395,
            V_18W30A = 394,
            V_1_13 = 393,
            V_1_13_PRE10 = 392,
            V_1_13_PRE9 = 391,
            V_1_13_PRE8 = 390,
            V_1_13_PRE7 = 389,
            V_1_13_PRE6 = 388,
            V_1_13_PRE5 = 387,
            V_1_13_PRE4 = 386,
            V_1_13_PRE3 = 385,
            V_1_13_PRE2 = 384,
            V_1_13_PRE1 = 383,
            V_18W22C = 382,
            V_18W22B = 381,
            V_18W22A = 380,
            V_18W21B = 379,
            V_18W21A = 378,
            V_18W20C = 377,
            V_18W20B = 376,
            V_18W20A = 375,
            V_18W19B = 374,
            V_18W19A = 373,
            V_18W16A = 372,
            V_18W15A = 371,
            V_18W14B = 370,
            V_18W14A = 369,
            V_18W11A = 368,
            V_18W10D = 367,
            V_18W10C = 366,
            V_18W10B = 365,
            V_18W10A = 364,
            V_18W09A = 363,
            V_18W08B = 362,
            V_18W08A = 361,
            V_18W07C = 360,
            V_18W07B = 359,
            V_18W07A = 358,
            V_18W06A = 357,
            V_18W05A = 356,
            V_18W03B = 355,
            V_18W03A = 354,
            V_18W02A = 353,
            V_18W01A = 352,
            V_17W50A = 351,
            V_17W49B = 350,
            V_17W49A = 349,
            V_17W48A = 348,
            V_17W47B = 347,
            V_17W47A = 346,
            V_17W46A = 345,
            V_17W45B = 344,
            V_17W45A = 343,
            V_17W43B = 342,
            V_17W43A = 341,
            V_1_12_2 = 340,
            V_1_12_2_PRE2 = 339,
            V_1_12_1 = 338,
            V_1_12_1_PRE1 = 337,
            V_17W31A = 336,
            V_1_12 = 335,
            V_1_12_PRE7 = 334,
            V_1_12_PRE6 = 333,
            V_1_12_PRE5 = 332,
            V_1_12_PRE4 = 331,
            V_1_12_PRE3 = 330,
            V_1_12_PRE2 = 329,
            V_1_12_PRE1 = 328,
            V_17W18B = 327,
            V_17W18A = 326,
            V_17W17B = 325,
            V_17W17A = 324,
            V_17W16B = 323,
            V_17W16A = 322,
            V_17W15A = 321,
            V_17W14A = 320,
            V_17W13B = 319,
            V_17W13A = 318,
            V_17W06A = 317,
            V_1_11_2 = 316,
            V_1_11 = 315,
            V_1_11_PRE1 = 314,
            V_16W44A = 313,
            V_16W42A = 312,
            V_16W41A = 311,
            V_16W40A = 310,
            V_16W39C = 309,
            V_16W39B = 308,
            V_16W39A = 307,
            V_16W38A = 306,
            V_16W36A = 305,
            V_16W35A = 304,
            V_16W33A = 303,
            V_16W32B = 302,
            V_16W32A = 301,
            V_1_10_2 = 210,
            V_1_10_PRE2 = 205,
            V_1_10_PRE1 = 204,
            V_16W21B = 203,
            V_16W21A = 202,
            V_16W20A = 201,
            V_1_9_4 = 110,
            V_1_9_3_PRE1 = 109,
            V_1_9_1 = 108,
            V_1_9_1_PRE1 = 107,
            V_1_9_PRE4 = 106,
            V_1_9_PRE3 = 105,
            V_1_9_PRE2 = 104,
            V_1_9_PRE1 = 103,
            V_16W07B = 102,
            V_16W07A = 101,
            V_16W06A = 100,
            V_16W05B = 99,
            V_16W05A = 98,
            V_16W04A = 97,
            V_16W03A = 96,
            V_16W02A = 95,
            V_15W51B = 94,
            V_15W51A = 93,
            V_15W50A = 92,
            V_15W49B = 91,
            V_15W49A = 90,
            V_15W47C = 89,
            V_15W47B = 88,
            V_15W47A = 87,
            V_15W46A = 86,
            V_15W45A = 85,
            V_15W44B = 84,
            V_15W44A = 83,
            V_15W43C = 82,
            V_15W43B = 81,
            V_15W43A = 80,
            V_15W42A = 79,
            V_15W41B = 78,
            V_15W41A = 77,
            V_15W40B = 76,
            V_15W40A = 75,
            V_15W39C = 74,
            V_15W38B = 73,
            V_15W38A = 72,
            V_15W37A = 71,
            V_15W36D = 70,
            V_15W36C = 69,
            V_15W36B = 68,
            V_15W36A = 67,
            V_15W35E = 66,
            V_15W35D = 65,
            V_15W35C = 64,
            V_15W35B = 63,
            V_15W35A = 62,
            V_15W34D = 61,
            V_15W34C = 60,
            V_15W34B = 59,
            V_15W34A = 58,
            V_15W33C = 57,
            V_15W33B = 56,
            V_15W33A = 55,
            V_15W32C = 54,
            V_15W32B = 53,
            V_15W32A = 52,
            V_15W31C = 51,
            V_15W31B = 50,
            V_15W31A = 49,
            V_1_8_9 = 47,
            V_1_8_PRE3 = 46,
            V_1_8_PRE2 = 45,
            V_1_8_PRE1 = 44,
            V_14W34D = 43,
            V_14W34C = 42,
            V_14W34B = 41,
            V_14W34A = 40,
            V_14W33C = 39,
            V_14W33B = 38,
            V_14W33A = 37,
            V_14W32D = 36,
            V_14W32C = 35,
            V_14W32B = 34,
            V_14W32A = 33,
            V_14W31A = 32,
            V_14W30C = 31,
            V_14W30B = 30,
            V_14W29A = 29,
            V_14W28B = 28,
            V_14W28A = 27,
            V_14W27B = 26,
            V_14W26C = 25,
            V_14W26B = 24,
            V_14W26A = 23,
            V_14W25B = 22,
            V_14W25A = 21,
            V_14W21B = 20,
            V_14W21A = 19,
            V_14W20B = 18,
            V_14W19A = 17,
            V_14W18B = 16,
            V_14W17A = 15,
            V_14W11B = 14,
            V_14W10C = 13,
            V_14W08A = 12,
            V_14W07A = 11,
            V_14W06B = 10,
            V_14W05B = 9,
            V_14W04B = 8,
            V_14W04A = 7,
            V_14W03B = 6,
            V_1_7_10 = 5,
            V_1_7_5 = 4,
            V_1_7_1_PRE = 3,
            V_13W43A = 2,
            V_13W42B = 1,
            V_13W41B = 0;

    public static final int LOWEST_VERSION_SUPPORTED = V_13W41B;
}