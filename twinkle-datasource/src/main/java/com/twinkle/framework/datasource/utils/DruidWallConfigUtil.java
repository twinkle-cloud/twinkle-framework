package com.twinkle.framework.datasource.utils;

import com.alibaba.druid.wall.WallConfig;
import com.twinkle.framework.datasource.spring.boot.autoconfigure.druid.DruidWallConfig;
import org.springframework.util.StringUtils;

/**
 * Function: Druid Wall Configuration Util. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/12/19 6:31 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public final class DruidWallConfigUtil {
    /**
     * Build Druid Wall Settings based on current settings and global settings.
     *
     * @param _current Current Settings.
     * @param _global  Global Settings.
     * @return
     */
    public static WallConfig toWallConfig(DruidWallConfig _current, DruidWallConfig _global) {
        WallConfig wallConfig = new WallConfig();

        String tempDir = StringUtils.isEmpty(_current.getDir()) ? _global.getDir() : _current.getDir();
        if (!StringUtils.isEmpty(tempDir)) {
            wallConfig.loadConfig(tempDir);
        }
        String tempTenantTablePattern =
                StringUtils.isEmpty(_current.getTenantTablePattern()) ? _global.getTenantTablePattern()
                        : _current.getTenantTablePattern();
        if (!StringUtils.isEmpty(tempTenantTablePattern)) {
            wallConfig.setTenantTablePattern(tempTenantTablePattern);
        }
        String tempTenantColumn =
                StringUtils.isEmpty(_current.getTenantColumn()) ? _global.getTenantColumn() : _current.getTenantColumn();
        if (!StringUtils.isEmpty(tempTenantColumn)) {
            wallConfig.setTenantTablePattern(tempTenantColumn);
        }
        Boolean tempNoneBaseStatementAllow =
                _current.getNoneBaseStatementAllow() == null ? _global.getNoneBaseStatementAllow()
                        : _current.getNoneBaseStatementAllow();
        if (tempNoneBaseStatementAllow != null && tempNoneBaseStatementAllow) {
            wallConfig.setNoneBaseStatementAllow(true);
        }
        Integer tempInsertValuesCheckSize =
                _current.getInsertValuesCheckSize() == null ? _global.getInsertValuesCheckSize()
                        : _current.getInsertValuesCheckSize();
        if (tempInsertValuesCheckSize != null) {
            wallConfig.setInsertValuesCheckSize(tempInsertValuesCheckSize);
        }
        Integer tempSelectLimit = _current.getSelectLimit() == null ? _global.getSelectLimit() : _current.getSelectLimit();
        if (tempSelectLimit != null) {
            _current.setSelectLimit(tempSelectLimit);
        }

        Boolean tempCallAllow = _current.getCallAllow() == null ? _global.getCallAllow() : _current.getCallAllow();
        if (tempCallAllow != null && !tempCallAllow) {
            wallConfig.setCallAllow(false);
        }
        Boolean tempSelectAllow = _current.getSelectAllow() == null ? _global.getSelectAllow() : _current.getSelectAllow();
        if (tempSelectAllow != null && !tempSelectAllow) {
            wallConfig.setSelelctAllow(false);
        }
        Boolean tempSelectIntoAllow =
                _current.getSelectIntoAllow() == null ? _global.getSelectIntoAllow() : _current.getSelectIntoAllow();
        if (tempSelectIntoAllow != null && !tempSelectIntoAllow) {
            wallConfig.setSelectIntoAllow(false);
        }
        Boolean tempSelectIntoOutfileAllow =
                _current.getSelectIntoOutfileAllow() == null ? _global.getSelectIntoOutfileAllow()
                        : _current.getSelectIntoOutfileAllow();
        if (tempSelectIntoOutfileAllow != null && tempSelectIntoOutfileAllow) {
            wallConfig.setSelectIntoOutfileAllow(true);
        }
        Boolean tempSelectWhereAlwayTrueCheck =
                _current.getSelectWhereAlwayTrueCheck() == null ? _global.getSelectWhereAlwayTrueCheck()
                        : _current.getSelectWhereAlwayTrueCheck();
        if (tempSelectWhereAlwayTrueCheck != null && !tempSelectWhereAlwayTrueCheck) {
            wallConfig.setSelectWhereAlwayTrueCheck(false);
        }
        Boolean tempSelectHavingAlwayTrueCheck =
                _current.getSelectHavingAlwayTrueCheck() == null ? _global.getSelectHavingAlwayTrueCheck()
                        : _current.getSelectHavingAlwayTrueCheck();
        if (tempSelectHavingAlwayTrueCheck != null && !tempSelectHavingAlwayTrueCheck) {
            wallConfig.setSelectHavingAlwayTrueCheck(false);
        }
        Boolean tempSelectUnionCheck =
                _current.getSelectUnionCheck() == null ? _global.getSelectUnionCheck() : _current.getSelectUnionCheck();
        if (tempSelectUnionCheck != null && !tempSelectUnionCheck) {
            wallConfig.setSelectUnionCheck(false);
        }
        Boolean tempSelectMinusCheck =
                _current.getSelectMinusCheck() == null ? _global.getSelectMinusCheck() : _current.getSelectMinusCheck();
        if (tempSelectMinusCheck != null && !tempSelectMinusCheck) {
            wallConfig.setSelectMinusCheck(false);
        }
        Boolean tempSelectExceptCheck =
                _current.getSelectExceptCheck() == null ? _global.getSelectExceptCheck() : _current.getSelectExceptCheck();
        if (tempSelectExceptCheck != null && !tempSelectExceptCheck) {
            wallConfig.setSelectExceptCheck(false);
        }
        Boolean tempSelectIntersectCheck =
                _current.getSelectIntersectCheck() == null ? _global.getSelectIntersectCheck()
                        : _current.getSelectIntersectCheck();
        if (tempSelectIntersectCheck != null && !tempSelectIntersectCheck) {
            wallConfig.setSelectIntersectCheck(false);
        }
        Boolean tempCreateTableAllow =
                _current.getCreateTableAllow() == null ? _global.getCreateTableAllow() : _current.getCreateTableAllow();
        if (tempCreateTableAllow != null && !tempCreateTableAllow) {
            wallConfig.setCreateTableAllow(false);
        }
        Boolean tempDropTableAllow =
                _current.getDropTableAllow() == null ? _global.getDropTableAllow() : _current.getDropTableAllow();
        if (tempDropTableAllow != null && !tempDropTableAllow) {
            wallConfig.setDropTableAllow(false);
        }
        Boolean tempAlterTableAllow =
                _current.getAlterTableAllow() == null ? _global.getAlterTableAllow() : _current.getAlterTableAllow();
        if (tempAlterTableAllow != null && !tempAlterTableAllow) {
            wallConfig.setAlterTableAllow(false);
        }
        Boolean tempRenameTableAllow =
                _current.getRenameTableAllow() == null ? _global.getRenameTableAllow() : _current.getRenameTableAllow();
        if (tempRenameTableAllow != null && !tempRenameTableAllow) {
            wallConfig.setRenameTableAllow(false);
        }
        Boolean tempHintAllow = _current.getHintAllow() == null ? _global.getHintAllow() : _current.getHintAllow();
        if (tempHintAllow != null && !tempHintAllow) {
            wallConfig.setHintAllow(false);
        }
        Boolean tempLockTableAllow =
                _current.getLockTableAllow() == null ? _global.getLockTableAllow() : _current.getLockTableAllow();
        if (tempLockTableAllow != null && !tempLockTableAllow) {
            wallConfig.setLockTableAllow(false);
        }
        Boolean tempStartTransactionAllow =
                _current.getStartTransactionAllow() == null ? _global.getStartTransactionAllow()
                        : _current.getStartTransactionAllow();
        if (tempStartTransactionAllow != null && !tempStartTransactionAllow) {
            wallConfig.setStartTransactionAllow(false);
        }
        Boolean tempBlockAllow = _current.getBlockAllow() == null ? _global.getBlockAllow() : _current.getBlockAllow();
        if (tempBlockAllow != null && !tempBlockAllow) {
            wallConfig.setBlockAllow(false);
        }
        Boolean tempConditionAndAlwayTrueAllow =
                _current.getConditionAndAlwayTrueAllow() == null ? _global.getConditionAndAlwayTrueAllow()
                        : _current.getConditionAndAlwayTrueAllow();
        if (tempConditionAndAlwayTrueAllow != null && tempConditionAndAlwayTrueAllow) {
            wallConfig.setConditionAndAlwayTrueAllow(true);
        }
        Boolean tempConditionAndAlwayFalseAllow =
                _current.getConditionAndAlwayFalseAllow() == null ? _global.getConditionAndAlwayFalseAllow()
                        : _current.getConditionAndAlwayFalseAllow();
        if (tempConditionAndAlwayFalseAllow != null && tempConditionAndAlwayFalseAllow) {
            wallConfig.setConditionAndAlwayFalseAllow(true);
        }
        Boolean tempConditionDoubleConstAllow =
                _current.getConditionDoubleConstAllow() == null ? _global.getConditionDoubleConstAllow()
                        : _current.getConditionDoubleConstAllow();
        if (tempConditionDoubleConstAllow != null && tempConditionDoubleConstAllow) {
            wallConfig.setConditionDoubleConstAllow(true);
        }
        Boolean tempConditionLikeTrueAllow =
                _current.getConditionLikeTrueAllow() == null ? _global.getConditionLikeTrueAllow()
                        : _current.getConditionLikeTrueAllow();
        if (tempConditionLikeTrueAllow != null && !tempConditionLikeTrueAllow) {
            wallConfig.setConditionLikeTrueAllow(false);
        }
        Boolean tempSelectAllColumnAllow =
                _current.getSelectAllColumnAllow() == null ? _global.getSelectAllColumnAllow()
                        : _current.getSelectAllColumnAllow();
        if (tempSelectAllColumnAllow != null && !tempSelectAllColumnAllow) {
            wallConfig.setSelectAllColumnAllow(false);
        }
        Boolean tempDeleteAllow = _current.getDeleteAllow() == null ? _global.getDeleteAllow() : _current.getDeleteAllow();
        if (tempDeleteAllow != null && !tempDeleteAllow) {
            wallConfig.setDeleteAllow(false);
        }
        Boolean tempDeleteWhereAlwayTrueCheck =
                _current.getDeleteWhereAlwayTrueCheck() == null ? _global.getDeleteWhereAlwayTrueCheck()
                        : _current.getDeleteWhereAlwayTrueCheck();
        if (tempDeleteWhereAlwayTrueCheck != null && !tempDeleteWhereAlwayTrueCheck) {
            wallConfig.setDeleteWhereAlwayTrueCheck(false);
        }
        Boolean tempDeleteWhereNoneCheck =
                _current.getDeleteWhereNoneCheck() == null ? _global.getDeleteWhereNoneCheck()
                        : _current.getDeleteWhereNoneCheck();
        if (tempDeleteWhereNoneCheck != null && tempDeleteWhereNoneCheck) {
            wallConfig.setDeleteWhereNoneCheck(true);
        }
        Boolean tempUpdateAllow = _current.getUpdateAllow() == null ? _global.getUpdateAllow() : _current.getUpdateAllow();
        if (tempUpdateAllow != null && !tempUpdateAllow) {
            wallConfig.setUpdateAllow(false);
        }
        Boolean tempUpdateWhereAlayTrueCheck =
                _current.getUpdateWhereAlayTrueCheck() == null ? _global.getUpdateWhereAlayTrueCheck()
                        : _current.getUpdateWhereAlayTrueCheck();
        if (tempUpdateWhereAlayTrueCheck != null && !tempUpdateWhereAlayTrueCheck) {
            wallConfig.setUpdateWhereAlayTrueCheck(false);
        }
        Boolean tempUpdateWhereNoneCheck =
                _current.getUpdateWhereNoneCheck() == null ? _global.getUpdateWhereNoneCheck()
                        : _current.getUpdateWhereNoneCheck();
        if (tempUpdateWhereNoneCheck != null && tempUpdateWhereNoneCheck) {
            wallConfig.setUpdateWhereNoneCheck(true);
        }
        Boolean tempInsertAllow = _current.getInsertAllow() == null ? _global.getInsertAllow() : _current.getInsertAllow();
        if (tempInsertAllow != null && !tempInsertAllow) {
            wallConfig.setInsertAllow(false);
        }
        Boolean tempMergeAllow = _current.getMergeAllow() == null ? _global.getMergeAllow() : _current.getMergeAllow();
        if (tempMergeAllow != null && !tempMergeAllow) {
            wallConfig.setMergeAllow(false);
        }
        Boolean tempMinusAllow = _current.getMinusAllow() == null ? _global.getMinusAllow() : _current.getMinusAllow();
        if (tempMinusAllow != null && !tempMinusAllow) {
            wallConfig.setMinusAllow(false);
        }
        Boolean tempIntersectAllow =
                _current.getIntersectAllow() == null ? _global.getIntersectAllow() : _current.getIntersectAllow();
        if (tempIntersectAllow != null && !tempIntersectAllow) {
            wallConfig.setIntersectAllow(false);
        }
        Boolean tempReplaceAllow =
                _current.getReplaceAllow() == null ? _global.getReplaceAllow() : _current.getReplaceAllow();
        if (tempReplaceAllow != null && !tempReplaceAllow) {
            wallConfig.setReplaceAllow(false);
        }
        Boolean tempSetAllow = _current.getSetAllow() == null ? _global.getSetAllow() : _current.getSetAllow();
        if (tempSetAllow != null && !tempSetAllow) {
            wallConfig.setSetAllow(false);
        }
        Boolean tempCommitAllow = _current.getCommitAllow() == null ? _global.getCommitAllow() : _current.getCommitAllow();
        if (tempCommitAllow != null && !tempCommitAllow) {
            wallConfig.setCommitAllow(false);
        }
        Boolean tempRollbackAllow =
                _current.getRollbackAllow() == null ? _global.getRollbackAllow() : _current.getRollbackAllow();
        if (tempRollbackAllow != null && !tempRollbackAllow) {
            wallConfig.setRollbackAllow(false);
        }
        Boolean tempUseAllow = _current.getUseAllow() == null ? _global.getUseAllow() : _current.getUseAllow();
        if (tempUseAllow != null && !tempUseAllow) {
            wallConfig.setUseAllow(false);
        }
        Boolean tempMultiStatementAllow =
                _current.getMultiStatementAllow() == null ? _global.getMultiStatementAllow()
                        : _current.getMultiStatementAllow();
        if (tempMultiStatementAllow != null && tempMultiStatementAllow) {
            wallConfig.setMultiStatementAllow(true);
        }
        Boolean tempTruncateAllow =
                _current.getTruncateAllow() == null ? _global.getTruncateAllow() : _current.getTruncateAllow();
        if (tempTruncateAllow != null && !tempTruncateAllow) {
            wallConfig.setTruncateAllow(false);
        }
        Boolean tempCommentAllow =
                _current.getCommentAllow() == null ? _global.getCommentAllow() : _current.getCommentAllow();
        if (tempCommentAllow != null && tempCommentAllow) {
            wallConfig.setCommentAllow(true);
        }
        Boolean tempStrictSyntaxCheck =
                _current.getStrictSyntaxCheck() == null ? _global.getStrictSyntaxCheck() : _current.getStrictSyntaxCheck();
        if (tempStrictSyntaxCheck != null && !tempStrictSyntaxCheck) {
            wallConfig.setStrictSyntaxCheck(false);
        }
        Boolean tempConstArithmeticAllow =
                _current.getConstArithmeticAllow() == null ? _global.getConstArithmeticAllow()
                        : _current.getConstArithmeticAllow();
        if (tempConstArithmeticAllow != null && !tempConstArithmeticAllow) {
            wallConfig.setConstArithmeticAllow(false);
        }
        Boolean tempLimitZeroAllow =
                _current.getLimitZeroAllow() == null ? _global.getLimitZeroAllow() : _current.getLimitZeroAllow();
        if (tempLimitZeroAllow != null && tempLimitZeroAllow) {
            wallConfig.setLimitZeroAllow(true);
        }
        Boolean tempDescribeAllow =
                _current.getDescribeAllow() == null ? _global.getDescribeAllow() : _current.getDescribeAllow();
        if (tempDescribeAllow != null && !tempDescribeAllow) {
            wallConfig.setDescribeAllow(false);
        }
        Boolean tempShowAllow = _current.getShowAllow() == null ? _global.getShowAllow() : _current.getShowAllow();
        if (tempShowAllow != null && !tempShowAllow) {
            wallConfig.setShowAllow(false);
        }
        Boolean tempSchemaCheck = _current.getSchemaCheck() == null ? _global.getSchemaCheck() : _current.getSchemaCheck();
        if (tempSchemaCheck != null && !tempSchemaCheck) {
            wallConfig.setSchemaCheck(false);
        }
        Boolean tempTableCheck = _current.getTableCheck() == null ? _global.getTableCheck() : _current.getTableCheck();
        if (tempTableCheck != null && !tempTableCheck) {
            wallConfig.setTableCheck(false);
        }
        Boolean tempFunctionCheck =
                _current.getFunctionCheck() == null ? _global.getFunctionCheck() : _current.getFunctionCheck();
        if (tempFunctionCheck != null && !tempFunctionCheck) {
            wallConfig.setFunctionCheck(false);
        }
        Boolean tempObjectCheck = _current.getObjectCheck() == null ? _global.getObjectCheck() : _current.getObjectCheck();
        if (tempObjectCheck != null && !tempObjectCheck) {
            wallConfig.setObjectCheck(false);
        }
        Boolean tempVariantCheck =
                _current.getVariantCheck() == null ? _global.getVariantCheck() : _current.getVariantCheck();
        if (tempVariantCheck != null && !tempVariantCheck) {
            wallConfig.setVariantCheck(false);
        }
        Boolean tempMustParameterized =
                _current.getMustParameterized() == null ? _global.getMustParameterized() : _current.getMustParameterized();
        if (tempMustParameterized != null && tempMustParameterized) {
            wallConfig.setMustParameterized(true);
        }
        Boolean tempDoPrivilegedAllow =
                _current.getDoPrivilegedAllow() == null ? _global.getDoPrivilegedAllow() : _current.getDoPrivilegedAllow();
        if (tempDoPrivilegedAllow != null && tempDoPrivilegedAllow) {
            wallConfig.setDoPrivilegedAllow(true);
        }
        Boolean tempWrapAllow = _current.getWrapAllow() == null ? _global.getWrapAllow() : _current.getWrapAllow();
        if (tempWrapAllow != null && !tempWrapAllow) {
            wallConfig.setWrapAllow(false);
        }
        Boolean tempMetadataAllow =
                _current.getMetadataAllow() == null ? _global.getMetadataAllow() : _current.getMetadataAllow();
        if (tempMetadataAllow != null && !tempMetadataAllow) {
            wallConfig.setMetadataAllow(false);
        }
        Boolean tempConditionOpXorAllow =
                _current.getConditionOpXorAllow() == null ? _global.getConditionOpXorAllow()
                        : _current.getConditionOpXorAllow();
        if (tempConditionOpXorAllow != null && tempConditionOpXorAllow) {
            wallConfig.setConditionOpXorAllow(true);
        }
        Boolean tempConditionOpBitwseAllow =
                _current.getConditionOpBitwseAllow() == null ? _global.getConditionOpBitwseAllow()
                        : _current.getConditionOpBitwseAllow();
        if (tempConditionOpBitwseAllow != null && !tempConditionOpBitwseAllow) {
            wallConfig.setConditionOpBitwseAllow(false);
        }
        Boolean tempCaseConditionConstAllow =
                _current.getCaseConditionConstAllow() == null ? _global.getCaseConditionConstAllow()
                        : _current.getCaseConditionConstAllow();
        if (tempCaseConditionConstAllow != null && tempCaseConditionConstAllow) {
            wallConfig.setCaseConditionConstAllow(true);
        }
        Boolean tempCompleteInsertValuesCheck =
                _current.getCompleteInsertValuesCheck() == null ? _global.getCompleteInsertValuesCheck()
                        : _current.getCompleteInsertValuesCheck();
        if (tempCompleteInsertValuesCheck != null && tempCompleteInsertValuesCheck) {
            wallConfig.setCompleteInsertValuesCheck(true);
        }
        return wallConfig;
    }
}
